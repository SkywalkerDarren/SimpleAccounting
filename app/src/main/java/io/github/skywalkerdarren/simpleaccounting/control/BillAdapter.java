package io.github.skywalkerdarren.simpleaccounting.control;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.BaseType;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.model.ExpenseType;
import io.github.skywalkerdarren.simpleaccounting.model.IncomeType;

import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.EXPENSE;
import static io.github.skywalkerdarren.simpleaccounting.model.BillLab.INCOME;

/**
 * Created by darren on 2018/2/12.
 *
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<BillAdapter.BillInfo, BaseViewHolder> {
    public static final int WITHOUT_REMARK = 0;
    public static final int WITH_REMARK = 1;
    public static final int HEADER = 2;


    public BillAdapter(List<BillInfo> bills) {
        super(bills);
        addItemType(WITH_REMARK, R.layout.list_bill_item);
        addItemType(WITHOUT_REMARK, R.layout.list_bill_item_without_remark);
        addItemType(HEADER, R.layout.list_bill_header);
    }

    public void setBills(List<BillInfo> bills) {
        setNewData(bills);
    }


    @Override
    protected void convert(BaseViewHolder helper, BillInfo item) {
        switch (item.getItemType()) {
            case WITH_REMARK:
                helper.setText(R.id.remark_text_view, item.getRemark());
            case WITHOUT_REMARK:
                boolean isExpense = item.isExpense();
                BaseType type = isExpense ? ExpenseType.getInstance() : IncomeType.getInstance();
                helper.setTextColor(R.id.balance_text_view, item.isExpense() ? Color.RED : Color.GREEN);
                helper.setImageResource(R.id.type_image_view, item.getBillTypeResId());
                helper.setText(R.id.title_text_view, item.getTitle());
                helper.setText(R.id.balance_text_view, item.getBalance());
                break;
            case HEADER:
                helper.setText(R.id.bills_date_text_view, item.getDateTime().toString("yyyy-MM-dd"));
                helper.setText(R.id.bill_expense_text_view, item.getExpense());
                helper.setText(R.id.bill_income_text_view, item.getIncome());
                break;
            default:
                break;
        }
    }


    public static class BillInfo implements MultiItemEntity {
        private int mType;

        private UUID mUUID;
        private String mTitle;
        private String mRemark;
        private String mBalance;
        private boolean mIsExpense;
        private String mBillTypeName;
        @DrawableRes
        private int mBillTypeResId;

        private String mIncome;
        private String mExpense;

        private DateTime mDateTime;

        public BillInfo(Bill bill) {
            mType = TextUtils.isEmpty(bill.getRemark()) ? WITHOUT_REMARK : WITH_REMARK;
            mUUID = bill.getId();
            mTitle = bill.getName();
            mRemark = bill.getRemark();
            mBalance = bill.getBalance().toString();
            mIsExpense = bill.isExpense();
            mBillTypeName = bill.getTypeName();
            mBillTypeResId = bill.getTypeResId();
            mDateTime = bill.getDate();
        }

        public BillInfo(DateHeaderDivider header) {
            mType = HEADER;
            mDateTime = header.getDate();
            mExpense = header.getExpense();
            mIncome = header.getIncome();
        }

        public static List<BillInfo> getBillInfoList(List<Bill> bills, BillLab billLab) {
            List<BillInfo> billInfoList = new ArrayList<>();
            // 上一个账单的年月日
            DateTime date = null;
            for (int i = 0; i < bills.size(); i++) {
                Bill bill = bills.get(i);
                DateTime dateTime = bill.getDate();
                int y = dateTime.getYear();
                int m = dateTime.getMonthOfYear();
                int d = dateTime.getDayOfMonth();
                // 当前账单的年月日
                DateTime currentDate = new DateTime(y, m, d, 0, 0);
                // 如果当前帐单与上一张单年月日不同，则添加账单
                if (date == null || !date.equals(currentDate)) {
                    date = currentDate;
                    BigDecimal income = billLab.getStatics(date, date.plusDays(1)).get(INCOME);
                    BigDecimal expense = billLab.getStatics(date, date.plusDays(1)).get(EXPENSE);
                    billInfoList.add(new BillInfo(new DateHeaderDivider(date, income, expense)));
                }
                billInfoList.add(new BillInfo(bill));

            }
            return billInfoList;
        }

        @Override
        public int getItemType() {
            return mType;
        }

        public String getRemark() {
            return mRemark;
        }

        @DrawableRes
        public int getBillTypeResId() {
            return mBillTypeResId;
        }

        public String getBillTypeName() {
            return mBillTypeName;
        }

        public UUID getUUID() {
            return mUUID;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getBalance() {
            return mBalance;
        }

        public boolean isExpense() {
            return mIsExpense;
        }

        public String getIncome() {
            return mIncome;
        }

        public String getExpense() {
            return mExpense;
        }

        public DateTime getDateTime() {
            return mDateTime;
        }

    }

}
