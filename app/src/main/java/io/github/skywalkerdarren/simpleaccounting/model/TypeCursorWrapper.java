package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.TypeDbSchema.TypeTable.Cols;

/**
 * @author darren
 * @date 2018/3/28
 */

public class TypeCursorWrapper extends CursorWrapper {
    public TypeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Type getType() {
        UUID id = UUID.fromString(getString(getColumnIndex(Cols.UUID)));
        Type type = new Type(id);
        type.setName(getString(getColumnIndex(Cols.NAME)));
        type.setResId(getInt(getColumnIndex(Cols.RES_ID)));
        type.setColorId(getInt(getColumnIndex(Cols.COLOR)));
        type.setExpense(getInt(getColumnIndex(Cols.IS_EXPENSE)) == 1);

        return type;
    }
}
