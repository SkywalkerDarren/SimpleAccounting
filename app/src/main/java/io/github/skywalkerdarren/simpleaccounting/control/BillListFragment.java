package io.github.skywalkerdarren.simpleaccounting.control;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

import static io.github.skywalkerdarren.simpleaccounting.control.BillAdapter.HEADER;
import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.EXPENSE;
import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.INCOME;

/**
 * Created by darren on 2018/1/31.
 */

public class BillListFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_DATE_TIME = 0;
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;
    private TextView mAddBillTextView;
    private BillLab mBillLab;
    private DateTime mDate;

    private TextView mIncomeTextView;
    private TextView mExpenseTextView;
    private TextView mBudgeTextView;
    private TextView mMonthIncomeTextView;
    private TextView mMonthExpenseTextView;
    private TextView mSetBudgeTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);
        mAddBillTextView = view.findViewById(R.id.add_bill_text_view);
        mBillListRecyclerView = view.findViewById(R.id.bill_recycle_view);
        mIncomeTextView = view.findViewById(R.id.money_income_text_view);
        mExpenseTextView = view.findViewById(R.id.money_expense_text_view);
        mBudgeTextView = view.findViewById(R.id.money_budge_text_view);
        mMonthIncomeTextView = view.findViewById(R.id.month_income_text_view);
        mMonthExpenseTextView = view.findViewById(R.id.month_expense_text_view);
        mSetBudgeTextView = view.findViewById(R.id.set_budge_text_view);
        mDate = DateTime.now();

        mMonthIncomeTextView.setOnClickListener(view1 -> {
            MonthPickerDialog monthPickerDialog = MonthPickerDialog.newInstance(mDate);
            monthPickerDialog.setTargetFragment(BillListFragment.this, REQUEST_DATE_TIME);
            monthPickerDialog.show(getFragmentManager(), "month picker");
        });


        mAddBillTextView.setOnClickListener(this);

        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        mBillListRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder(HEADER).create());
        return view;
    }

    /**
     * UI刷新
     * 放置经常需要刷新的视图
     */
    public void updateUI() {
        List<Bill> bills = mBillLab.getsBills(mDate.getYear(), mDate.monthOfYear().get());
        List<BillAdapter.BillInfo> billInfoList = BillAdapter.BillInfo.getBillInfoList(bills, mBillLab);

        DateTime month = new DateTime(mDate.getYear(), mDate.getMonthOfYear(), 1, 0, 0);
        mIncomeTextView.setText(mBillLab.getStatics(month, month.plusMonths(1)).get(INCOME).toString());
        mExpenseTextView.setText(mBillLab.getStatics(month, month.plusMonths(1)).get(EXPENSE).toString());
        // TODO 设置预算
        mBudgeTextView.setText("0");

        mMonthIncomeTextView.setText(mDate.getMonthOfYear() + getString(R.string.month_income));
        mMonthExpenseTextView.setText(mDate.getMonthOfYear() + getString(R.string.month_expense));
        mSetBudgeTextView.setText("设置预算");

        // 设定空布局
        if (billInfoList.size() < 1) {
            mAddBillTextView.setVisibility(View.VISIBLE);
            mBillListRecyclerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            mAddBillTextView.setVisibility(View.GONE);
            mBillListRecyclerView.setVisibility(View.VISIBLE);
        }
        // 刷新列表
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(billInfoList);
            mBillAdapter.openLoadAnimation(view -> new Animator[]{
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
            });
            mBillAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (adapter.getItemViewType(position) != HEADER) {
                    BillAdapter.BillInfo billInfo = (BillAdapter.BillInfo) adapter.getData().get(position);
                    UUID billId = billInfo.getUUID();
                    Intent intent = BillPagerDetailActivity
                            .newIntent(getActivity(), mBillLab.getBill(billId));
                    ActivityOptionsCompat options = getElementAnimator(view);
                    startActivity(intent, options.toBundle());
                }
            });
            mBillListRecyclerView.setAdapter(mBillAdapter);
        } else {
            mBillAdapter.setBills(billInfoList);
            mBillAdapter.notifyDataSetChanged();
        }


    }

    @NonNull
    private ActivityOptionsCompat getElementAnimator(View view) {
        Pair<View, String> imagePair = new Pair<>(
                view.findViewById(R.id.type_image_view),
                "type_image_view");
        Pair<View, String> balancePair = new Pair<>(
                view.findViewById(R.id.balance_text_view),
                "balance_text_view");
        Pair<View, String> titlePair = new Pair<>(
                view.findViewById(R.id.title_text_view),
                "title_text_view"
        );
        return ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(),
                        imagePair);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBillLab = BillLab.getInstance(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static BillListFragment newInstance() {

        Bundle args = new Bundle();

        BillListFragment fragment = new BillListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_bill_text_view:
                addBill();
                updateUI();
                break;
            default:
                break;
        }
    }

    /**
     * 新建账单
     */
    public void addBill() {
        Bill bill = new Bill();
        Intent intent = BillEditActivity.newIntent(getActivity(), bill);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == REQUEST_DATE_TIME) {
            DateTime dateTime = (DateTime) data.getSerializableExtra(MonthPickerDialog.EXTRA_DATE);
            mDate = dateTime;
            updateUI();
        }
    }
}
