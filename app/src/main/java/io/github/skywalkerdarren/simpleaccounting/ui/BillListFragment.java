package io.github.skywalkerdarren.simpleaccounting.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.EmptyLayoutBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillListBinding;
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
    private static final String SHARED_BUDGET = "budget";
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;

    FragmentBillListBinding mBinding;
    BillListViewModel mViewModel;

    private SharedPreferences mSharedPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_list, container, false);

        mViewModel = new BillListViewModel(getContext());
        mBinding.setBillList(mViewModel);

        mBillListRecyclerView = mBinding.billRecycleView;

        mSharedPref = getContext().getSharedPreferences(SHARED_BUDGET, Context.MODE_PRIVATE);
        mViewModel.setDate(DateTime.now());

        mBinding.dateImageView.setOnClickListener(view1 -> {
            MonthPickerDialog monthPickerDialog = MonthPickerDialog.newInstance(mViewModel.getDate());
            monthPickerDialog.setTargetFragment(BillListFragment.this, REQUEST_DATE_TIME);
            monthPickerDialog.show(getFragmentManager(), "month picker");
        });


        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO: 2018/3/25 预算选择逻辑 新小窗口
        mBinding.setBudgeTextView.setOnClickListener(view -> {

        });

        return mBinding.getRoot();
    }

    /**
     * UI刷新
     * 放置经常需要刷新的视图
     */
    @Override
    public void updateUI() {
        mViewModel.notifyChange();
        mBinding.moneyBudgeTextView.setText(mSharedPref.getString(SHARED_BUDGET, "0"));
        List<BillInfo> billInfoList = BillInfo
                .getBillInfoList(mViewModel.getDate().getYear(),
                        mViewModel.getDate().getMonthOfYear(), getActivity());
        updateAdapter(billInfoList);
    }

    /**
     * 刷新列表
     *
     * @param billInfoList 新数据
     */
    private void updateAdapter(List<BillInfo> billInfoList) {
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(billInfoList, getActivity());
            configAdapter();
            mBillListRecyclerView.setAdapter(mBillAdapter);
            mBillListRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration
                    .Builder(HEADER)
                    .create());
        } else {
            mBillAdapter.setBills(billInfoList);
            mBillAdapter.notifyDataSetChanged();
//            runLayoutAnimation(mBillListRecyclerView);
        }
    }

    /**
     * 当没有账单的时候的空视图
     *
     * @return 空视图
     */
    private View emptyView() {
        EmptyLayoutBinding binding = EmptyLayoutBinding.inflate(LayoutInflater.from(getContext()));
        binding.setEmpty(new EmptyListViewModel(getContext()));
        binding.getRoot().setOnClickListener(view -> updateUI());
        return binding.getRoot();
    }

    /**
     * 为适配器做详细设置
     */
    private void configAdapter() {
        mBillAdapter.setEmptyView(emptyView());
//        mBillAdapter.openLoadAnimation(view -> {
//            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
//            animator.setDuration(50);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            return new Animator[]{animator};
//        });
//        mBillAdapter.isFirstOnly(true);
    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE_TIME:
                mViewModel.setDate((DateTime) data
                        .getSerializableExtra(MonthPickerDialog.EXTRA_DATE));
                updateUI();
                break;
            default:
                break;
        }
    }
}
