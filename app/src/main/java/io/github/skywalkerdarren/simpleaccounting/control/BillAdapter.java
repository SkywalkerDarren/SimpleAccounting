package io.github.skywalkerdarren.simpleaccounting.control;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;

import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.WITHOUT_REMARK;
import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.WITH_REMARK;

/**
 * Created by darren on 2018/2/12.
 *
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<Bill, BaseViewHolder> implements StickyRecyclerHeadersAdapter<BaseViewHolder> {

    public BillAdapter(List<Bill> bills) {
        super(bills);
        addItemType(WITH_REMARK, R.layout.list_bill_item);
        addItemType(WITHOUT_REMARK, R.layout.list_bill_item_without_remark);
    }

    //    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view;
//        if (viewType == WITH_REMARK) {
//            view = inflater.inflate(R.layout.list_bill_item, parent, false);
//        } else {
//            view = inflater.inflate(R.layout.list_bill_item_without_remark, parent, false);
//        }
//        return new BaseViewHolder(view);
//    }

    @Override
    public int getItemViewType(int position) {
        String remark = getData().get(position).getRemark();
        if (TextUtils.isEmpty(remark)) {
            return WITHOUT_REMARK;
        } else {
            return WITH_REMARK;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Bill item) {
        switch (helper.getItemViewType()) {
            case WITH_REMARK:
                helper.setText(R.id.remark_text_view, item.getRemark());
            case WITHOUT_REMARK:
                helper.setText(R.id.balance_text_view, item.getBalance().toString());
                helper.setText(R.id.title_text_view, item.getName());
                helper.setTextColor(R.id.balance_text_view, item.isExpense() ? Color.RED : Color.GREEN);
                break;
            default:
                break;
        }
    }

    private void createBill() {
        // TODO 列表点击
    }

    @Override
    public long getHeaderId(int position) {
        List<Bill> billInfos = getData();
        DateTime dateTime = billInfos.get(position).getDate();
        int y = dateTime.getYear();
        int m = dateTime.getMonthOfYear();
        int d = dateTime.getDayOfMonth();
        return new DateTime(y, m, d, 0, 0, 0).getMillis();
    }

    @Override
    public BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bill_header, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int position) {
        // TODO 分隔栏
        Bill bill = getItem(position);
        holder.setText(R.id.bills_date_text_view, bill.getDate().toString("yyyy-MM-dd"));
        holder.setText(R.id.bill_expense_text_view, bill.getBalance().toString());
        holder.setText(R.id.bill_income_text_view, bill.getBalance().toString());
    }
}
