package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.TypeTable;

/**
 * @author darren
 * @date 2018/3/31
 */

class StatsCursorWrapper extends CursorWrapper {
    public StatsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Type getType() {
        return new Type(UUID.fromString(getString(getColumnIndex(TypeTable.Cols.UUID))))
                .setName(getString(getColumnIndex(TypeTable.Cols.NAME)))
                .setResId(getInt(getColumnIndex(TypeTable.Cols.RES_ID)))
                .setColorId(getInt(getColumnIndex(TypeTable.Cols.COLOR)))
                .setExpense(getInt(getColumnIndex(TypeTable.Cols.IS_EXPENSE)) == 1);
    }

    public Bill getBill() {
        return new Bill(UUID.fromString(getString(getColumnIndex(DbSchema.BillTable.Cols.UUID))))
                .setName(getString(getColumnIndex(DbSchema.BillTable.Cols.NAME)))
                .setBalance(new BigDecimal(getString(getColumnIndex(DbSchema.BillTable.Cols.BALANCE))))
                .setDate(new DateTime(getString(getColumnIndex(DbSchema.BillTable.Cols.DATE))))
                .setRemark(getString(getColumnIndex(DbSchema.BillTable.Cols.REMARK)))
                .setTypeId(UUID.fromString(getString(getColumnIndex(DbSchema.BillTable.Cols.TYPE_ID))));
    }
}
