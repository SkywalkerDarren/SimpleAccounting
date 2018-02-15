package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by darren on 2018/1/30.
 * 账单游标封装
 */

public class BillCursorWrapper extends CursorWrapper {
    public BillCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * 根据数据库游标数据创建对应账单
     *
     * @return 对应账单
     */
    public Bill getBill() {
        String uuid = getString(getColumnIndex(BillDbSchema.BillTable.Cols.UUID));
        String name = getString(getColumnIndex(BillDbSchema.BillTable.Cols.NAME));
        String balance = getString(getColumnIndex(BillDbSchema.BillTable.Cols.BALANCE));
        long date = getLong(getColumnIndex(BillDbSchema.BillTable.Cols.DATE));
        int isExpense = getInt(getColumnIndex(BillDbSchema.BillTable.Cols.IS_EXPENSE));
        String type = getString(getColumnIndex(BillDbSchema.BillTable.Cols.TYPE));
        String remark = getString(getColumnIndex(BillDbSchema.BillTable.Cols.REMARK));

        Bill bill = new Bill(UUID.fromString(uuid));
        bill.setName(name);
        bill.setBalance(new BigDecimal(balance));
        bill.setDate(new DateTime(date));
        bill.setRemark(remark);
        bill.setType(type);
        bill.setExpense(isExpense > 0);

        return bill;
    }
}
