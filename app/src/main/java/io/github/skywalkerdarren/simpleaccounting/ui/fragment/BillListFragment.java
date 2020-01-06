package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.EmptyLayoutBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillListBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillListViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.EmptyListViewModel;

import static io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter.HEADER;

/**
 * 账单列表fragment
 *
 * @author darren
 * @date 2018/1/31
 */

public class BillListFragment extends BaseFragment {
    private static final int REQUEST_DATE_TIME = 0;
    private FragmentBillListBinding mBinding;
    private BillListViewModel mViewModel;
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;
    private DateTime mDateTime;

    /**
     * 账单示例构造
     *
     * @return 当前账单
     */
    public static BillListFragment newInstance() {

        Bundle args = new Bundle();

        BillListFragment fragment = new BillListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_list, container, false);

        mBillListRecyclerView = mBinding.billRecycleView;

        mBinding.dateImageView.setOnClickListener(view1 -> {
            MonthPickerDialog monthPickerDialog = MonthPickerDialog.newInstance();
            monthPickerDialog.setTargetFragment(BillListFragment.this, REQUEST_DATE_TIME);
            monthPickerDialog.show(requireFragmentManager(), "month picker");
            mViewModel.getDate().observe(this, dateTime -> {
                monthPickerDialog.setYearPicker(dateTime.getYear());
                monthPickerDialog.setMonthPicker(dateTime.getMonthOfYear());
            });

        });


        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // TODO: 2018/3/25 预算选择逻辑 新小窗口
        mBinding.setBudgeTextView.setOnClickListener(view -> {

        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(BillListViewModel.class);
        mBinding.setBillList(mViewModel);
        mBinding.setLifecycleOwner(this);
    }

    /**
     * UI刷新
     * 放置经常需要刷新的视图
     */
    @Override
    public void updateUI() {
        String budget = PreferenceUtil.getString(requireContext(), PreferenceUtil.BUDGET, "0");
        mBinding.moneyBudgeTextView.setText(budget);
        mViewModel.getDate().observe(this, dateTime ->
                mViewModel.getBillInfoList().observe(this, this::updateAdapter));
        if (mDateTime == null) {
            mDateTime = DateTime.now();
        }
        mViewModel.setDate(mDateTime);
    }

    /**
     * 刷新列表
     *
     * @param billInfoList 新数据
     */
    private void updateAdapter(List<BillInfo> billInfoList) {
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(billInfoList, requireActivity());
            configAdapter();
            mBillListRecyclerView.setAdapter(mBillAdapter);
            mBillListRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration
                    .Builder(HEADER)
                    .create());
        } else {
            mBillAdapter.setNewData(billInfoList);
        }
    }

    /**
     * 当没有账单的时候的空视图
     *
     * @return 空视图
     */
    private View emptyView() {
        EmptyLayoutBinding binding = EmptyLayoutBinding.inflate(LayoutInflater.from(requireContext()));
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        binding.setEmpty(ViewModelProviders.of(this, factory).get(EmptyListViewModel.class));
        binding.getRoot().setOnClickListener(view -> updateUI());
        return binding.getRoot();
    }

    /**
     * 为适配器做详细设置
     */
    private void configAdapter() {
        mBillAdapter.setEmptyView(emptyView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE_TIME:
                mViewModel.setDate((DateTime) data
                        .getSerializableExtra(MonthPickerDialog.EXTRA_DATE));
                mViewModel.getDate().observe(this, dateTime -> {
                    mDateTime = dateTime;
                    mViewModel.getBillInfoList().observe(this, this::updateAdapter);
                });
                break;
            default:
                break;
        }
    }
}
