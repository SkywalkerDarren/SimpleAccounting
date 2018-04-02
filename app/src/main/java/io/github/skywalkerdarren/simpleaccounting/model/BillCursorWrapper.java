package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.BillTable.Cols;

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
        return new Bill(UUID.fromString(getString(getColumnIndex(Cols.UUID))))
                .setName(getString(getColumnIndex(Cols.NAME)))
                .setBalance(new BigDecimal(getString(getColumnIndex(Cols.BALANCE))))
                .setDate(new DateTime(getLong(getColumnIndex(Cols.DATE))))
                .setRemark(getString(getColumnIndex(Cols.REMARK)))
                .setAccountId(UUID.fromString(getString(getColumnIndex(Cols.ACCOUNT_ID))))
                .setTypeId(UUID.fromString(getString(getColumnIndex(Cols.TYPE_ID))));
    }
}
