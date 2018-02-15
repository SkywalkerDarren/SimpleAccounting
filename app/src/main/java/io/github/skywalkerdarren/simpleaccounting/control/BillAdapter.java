package io.github.skywalkerdarren.simpleaccounting.control;

import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.HEADER;
import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.HEAD_DIVIDER;
import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.WITHOUT_REMARK;
import static io.github.skywalkerdarren.simpleaccounting.control.BillEntity.WITH_REMARK;

/**
 * Created by darren on 2018/2/12.
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<BillEntity.BillInfo, BaseViewHolder> {


    public BillAdapter(List<BillEntity.BillInfo> bills) {
        super(bills);
        addItemType(WITH_REMARK, R.layout.list_bill_item);
        addItemType(WITHOUT_REMARK, R.layout.list_bill_item_without_remark);
        addItemType(HEADER, R.layout.list_bill_header);
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
        } else if (HEAD_DIVIDER.equals(remark)) {
            return HEADER;
        } else {
            return WITH_REMARK;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, BillEntity.BillInfo item) {
        switch (helper.getItemViewType()) {
            case WITH_REMARK:
                helper.setText(R.id.remark_text_view, item.getRemark());
            case WITHOUT_REMARK:
                helper.setText(R.id.balance_text_view, item.getBalance().toString());
                helper.setText(R.id.title_text_view, item.getName());
                helper.setTextColor(R.id.balance_text_view, item.isExpense() ? Color.RED : Color.GREEN);
                break;
            case HEADER:
                String date = item.getDate().toString("yyyy-MM-dd");
                helper.setText(R.id.bills_date_text_view, date);
                helper.setText(R.id.bill_income_text_view, item.getIncome().toString());
                helper.setText(R.id.bill_expense_text_view, item.getExpense().toString());
                break;
            default:
                break;
        }
    }

    private void createBill() {
        // TODO 列表点击
    }
}
