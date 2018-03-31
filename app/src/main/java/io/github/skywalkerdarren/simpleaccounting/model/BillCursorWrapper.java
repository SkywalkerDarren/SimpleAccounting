package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 账单游标封装
 *
 * @author darren
 * @date 2018/1/30
 */

class BillCursorWrapper extends CursorWrapper {
    BillCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * 根据数据库游标数据创建对应账单
     *
     * @return 对应账单
     */
    Bill getBill() {
        String uuid = getString(getColumnIndex(DbSchema.BillTable.Cols.UUID));
        String name = getString(getColumnIndex(DbSchema.BillTable.Cols.NAME));
        String balance = getString(getColumnIndex(DbSchema.BillTable.Cols.BALANCE));
        long date = getLong(getColumnIndex(DbSchema.BillTable.Cols.DATE));
        String type = getString(getColumnIndex(DbSchema.BillTable.Cols.TYPE_ID));
        String remark = getString(getColumnIndex(DbSchema.BillTable.Cols.REMARK));

        Bill bill = new Bill(UUID.fromString(uuid));
        bill.setName(name);
        bill.setBalance(new BigDecimal(balance));
        bill.setDate(new DateTime(date));
        bill.setRemark(remark);
        bill.setTypeId(UUID.fromString(type));
        return bill;
    }
}
