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
        return new Bill(UUID.fromString(getString(getColumnIndex(DbSchema.BillTable.Cols.UUID))))
                .setName(getString(getColumnIndex(DbSchema.BillTable.Cols.NAME)))
                .setBalance(new BigDecimal(getString(getColumnIndex(DbSchema.BillTable.Cols.BALANCE))))
                .setDate(new DateTime(getLong(getColumnIndex(DbSchema.BillTable.Cols.DATE))))
                .setRemark(getString(getColumnIndex(DbSchema.BillTable.Cols.REMARK)))
                .setTypeId(UUID.fromString(getString(getColumnIndex(DbSchema.BillTable.Cols.TYPE_ID))));
    }
}
