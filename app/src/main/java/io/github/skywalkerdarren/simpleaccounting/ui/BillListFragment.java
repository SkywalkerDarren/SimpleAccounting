package io.github.skywalkerdarren.simpleaccounting.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter;
import io.github.skywalkerdarren.simpleaccounting.adapter.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

import static io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter.HEADER;

/**
 * 账单列表fragment
 *
 * @author darren
 * @date 2018/1/31
 */

public class BillListFragment extends BaseFragment implements View.OnTouchListener {
    private static final int REQUEST_DATE_TIME = 0;
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;
    private BillLab mBillLab;
    private DateTime mDate;

    private TextView mIncomeTextView;
    private TextView mExpenseTextView;
    private TextView mBudgeTextView;
    private TextView mMonthIncomeTextView;
    private TextView mMonthExpenseTextView;
    private TextView mSetBudgeTextView;
    private int mX;
    private int mY;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBillLab = BillLab.getInstance(getActivity());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);
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


        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        mBillListRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder(HEADER).create());

        mBillListRecyclerView.setOnTouchListener(this);
        return view;
    }

    /**
     * UI刷新
     * 放置经常需要刷新的视图
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void updateUI() {

        DateTime month = new DateTime(mDate.getYear(), mDate.getMonthOfYear(), 1, 0, 0);
        mIncomeTextView.setText(mBillLab.getStats(month, month.plusMonths(1)).getIncome().toString());
        mExpenseTextView.setText(mBillLab.getStats(month, month.plusMonths(1)).getExpense().toString());
        // TODO 设置预算
        mBudgeTextView.setText("0");
        mMonthIncomeTextView.setText(mDate.getMonthOfYear() + getString(R.string.month_income));
        mMonthExpenseTextView.setText(mDate.getMonthOfYear() + getString(R.string.month_expense));
        mSetBudgeTextView.setText("设置预算");

        List<BillInfo> billInfoList = BillInfo
                .getBillInfoList(mDate.getYear(), mDate.getMonthOfYear(), getActivity());

        updateAdapter(billInfoList);
    }

    /**
     * 刷新列表
     *
     * @param billInfoList 新数据
     */
    private void updateAdapter(List<BillInfo> billInfoList) {
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(billInfoList);
            configAdapter();
            mBillListRecyclerView.setAdapter(mBillAdapter);
        } else {
            mBillAdapter.setBills(billInfoList);
            mBillAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当没有账单的时候的空视图
     *
     * @return 空视图
     */
    private View emptyView() {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null);
        TextView addTextView = v.findViewById(R.id.add_bill_text_view);
        ImageView addImageView = v.findViewById(R.id.add_bill_image_view);
        View.OnClickListener addBillListener = view -> {
            addBill();
            updateUI();
        };
        addImageView.setOnClickListener(addBillListener);
        addTextView.setOnClickListener(addBillListener);
        return v;
    }

    /**
     * 为适配器做详细设置
     */
    private void configAdapter() {
        mBillAdapter.setEmptyView(emptyView());
        mBillAdapter.openLoadAnimation(new AlphaInAnimation());
        mBillAdapter.isFirstOnly(false);
        mBillAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (adapter.getItemViewType(position) != HEADER) {
                clickBillItem(adapter, view, position);
            }
        });
        mBillAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            View v = adapter.getViewByPosition(mBillListRecyclerView, position, R.id.bill_item);
            clickBillItem(adapter, v, position);
        });
    }

    /**
     * 点击账单列表事件
     */
    private void clickBillItem(BaseQuickAdapter adapter, View view, int position) {
        BillInfo billInfo = (BillInfo) adapter.getData().get(position);
        UUID billId = billInfo.getUUID();
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        View center = view.findViewById(R.id.image_card_view);
        center.getLocationInWindow(location1);
        center.getLocationOnScreen(location2);
        mX = mBillAdapter.getX();
        mY = mBillAdapter.getY();
        // TODO 颜色
        Intent intent = BillDetailPagerActivity
                .newIntent(getActivity(), mBillLab.getBill(billId), mX, mY, R.color.orangea200);

        intent.putExtra(BillDetailPagerActivity.EXTRA_START_COLOR, R.color.orangea200);
        ActivityOptionsCompat options = getElementAnimator(view);
        startActivity(intent, options.toBundle());
    }

    /**
     * 设置共享元素转场动画
     * @param view 转场视图
     * @return 动画包
     */
    @NonNull
    private ActivityOptionsCompat getElementAnimator(View view) {
        Pair<View, String> imagePair = new Pair<>(
                view.findViewById(R.id.type_image_view),
                "type_image_view");
        return ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imagePair);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill, menu);
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

    /**
     * 新建账单
     */
    public void addBill() {
        Bill bill = new Bill();
        Intent intent = BillEditActivity.newIntent(getActivity(), bill,
                mBillAdapter.getX(), mBillAdapter.getY());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE_TIME:
                mDate = (DateTime) data.getSerializableExtra(MonthPickerDialog.EXTRA_DATE);
                updateUI();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mX = (int) motionEvent.getRawX();
        mY = (int) motionEvent.getRawY();
        return false;
    }
}
