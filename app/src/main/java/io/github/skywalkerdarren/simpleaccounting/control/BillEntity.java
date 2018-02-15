package io.github.skywalkerdarren.simpleaccounting.control;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * Created by darren on 2018/2/12.
 * 账单实体类
 * 账单库的基础上插入了分隔栏
 */

public class BillEntity {
    public static final int WITHOUT_REMARK = 0;
    public static final int WITH_REMARK = 1;
    public static final int HEADER = 2;
    public static final String HEAD_DIVIDER = "__分隔栏__";

    private List<BillInfo> mBillInfos;

    /**
     * 在bills中添加分隔栏
     *
     * @param bills 账单列
     * @param lab   账单库I
     * @return bill封装后的billInfo
     */
    public List<BillInfo> getBillsWithDayDivider(List<Bill> bills, BillLab lab) {
        mBillInfos = new ArrayList<>();
        DateTime headDay = DateTime.now();
        DateTime billDay;
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            int y = bill.getDate().getYear();
            int m = bill.getDate().getMonthOfYear();
            int d = bill.getDate().getDayOfMonth();
            billDay = bill.getDate();
            if (i == 0) {
                headDay = getDateTime(lab, y, m, d);
            } else if (headDay.getMillis() > billDay.getMillis()) {
                headDay = getDateTime(lab, y, m, d);
            }
            mBillInfos.add(new BillInfo(bill));
        }
        return mBillInfos;
    }

    @NonNull
    private DateTime getDateTime(BillLab lab, int y, int m, int d) {
        DateTime headDay;
        headDay = new DateTime(y, m, d, 0, 0, 0);
        BillInfo info = new BillInfo();
        info.setDate(headDay);
        info.setRemark(HEAD_DIVIDER);
        info.mIncome = lab.getDayStatics(y, m, d).get("income");
        info.mExpense = lab.getDayStatics(y, m, d).get("expense");
        mBillInfos.add(info);
        return headDay;
    }

    public static class BillInfo extends Bill implements MultiItemEntity {
        public BillInfo(Bill bill) {
            super(bill.getId());
            this.setName(bill.getName());
            this.setType(bill.getType());
            this.setDate(bill.getDate());
            this.setBalance(bill.getBalance());
            this.setRemark(bill.getRemark());
            this.setExpense(bill.isExpense());
        }

        public BillInfo() {
        }

        private BigDecimal mIncome;
        private BigDecimal mExpense;

        public BigDecimal getIncome() {
            return mIncome;
        }

        public BigDecimal getExpense() {
            return mExpense;
        }

        @Override
        public int getItemType() {
            String remark = super.getRemark();

            if (TextUtils.isEmpty(remark)) {
                return WITHOUT_REMARK;
            } else {
                if (HEAD_DIVIDER.equals(remark)) {
                    return HEADER;
                } else {
                    return WITH_REMARK;
                }
            }
        }

    }
}
