package io.github.skywalkerdarren.simpleaccounting.control;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.EXPENSE;
import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.INCOME;

/**
 * Created by darren on 2018/2/12.
 *
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> implements
        StickyRecyclerHeadersAdapter<BillAdapter.HeaderViewHolder> {
    public static final int WITHOUT_REMARK = 0;
    public static final int WITH_REMARK = 1;
    private List<Bill> mBills;
    private List<Integer> mHeadPositions;
    public BillAdapter(List<Bill> bills) {
        mBills = bills;
        mHeadPositions = getDividePosition(mBills);
    }

    public void setBills(List<Bill> bills) {
        mBills = bills;
        mHeadPositions = getDividePosition(mBills);
        notifyDataSetChanged();
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case WITH_REMARK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_item, parent, false);
                return new BillViewHolder(view);
            case WITHOUT_REMARK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_item_without_remark, parent, false);
                return new BillViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Bill bill = mBills.get(position);
        View view = holder.itemView;
        switch (getItemViewType(position)) {
            case WITH_REMARK:
                TextView textViewRemark = view.findViewById(R.id.remark_text_view);
                textViewRemark.setText(bill.getRemark());
            case WITHOUT_REMARK:
                TextView textViewTitle = view.findViewById(R.id.title_text_view);
                TextView textViewBalance = view.findViewById(R.id.balance_text_view);
                textViewTitle.setText(bill.getName());
                textViewBalance.setText(bill.getBalance().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String remark = mBills.get(position).getRemark();
        if (TextUtils.isEmpty(remark)) {
            return WITHOUT_REMARK;
        } else {
            return WITH_REMARK;
        }
    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    @Override
    public long getHeaderId(int position) {
        return mHeadPositions.get(position);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        // TODO 分隔栏
        Bill bill = mBills.get(position);
        DateTime dateTime = bill.getDate();
        int y = dateTime.getYear();
        int m = dateTime.getMonthOfYear();
        int d = dateTime.getDayOfMonth();
        Map<String, BigDecimal> statics = BillLab.getInstance(holder.itemView.getContext()).getDayStatics(y, m, d);
        View view = holder.itemView;
        TextView textViewDate = view.findViewById(R.id.bills_date_text_view);
        TextView textViewExpense = view.findViewById(R.id.bill_expense_text_view);
        TextView textViewIncome = view.findViewById(R.id.bill_income_text_view);
        textViewDate.setText(bill.getDate().toString("yyyy-MM-dd"));
        textViewExpense.setText(statics.get(EXPENSE).toString());
        textViewIncome.setText(statics.get(INCOME).toString());

    }

    private List<Integer> getDividePosition(List<Bill> bills) {
        List<Integer> positionList = new LinkedList<>();
        DateTime date = null;
        int t = 0;
        for (int i = 0; i < bills.size(); i++) {
            DateTime dateTime = bills.get(i).getDate();
            int y = dateTime.getYear();
            int m = dateTime.getMonthOfYear();
            int d = dateTime.getDayOfMonth();
            DateTime currentDate = new DateTime(y, m, d, 0, 0);
            if (date == null || !date.equals(currentDate)) {
                date = currentDate;
                t = i;
            }
            positionList.add(t);
        }
        return positionList;
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        public BillViewHolder(View view) {
            super(view);
        }
    }

    class HeaderViewHolder extends BillViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }
}
