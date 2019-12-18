package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.TypeTable.Cols;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;


/**
 * @author darren
 * @date 2018/3/28
 */

class TypeCursorWrapper extends CursorWrapper {
    TypeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    Type getType() {
        UUID id = UUID.fromString(getString(getColumnIndex(Cols.UUID)));
        return new Type(id)
                .setName(getString(getColumnIndex(Cols.NAME)))
                .setAssetsName(getString(getColumnIndex(Cols.IMAGE)))
                .setColorId(getInt(getColumnIndex(Cols.COLOR)))
                .setExpense(getInt(getColumnIndex(Cols.IS_EXPENSE)) == 1);
    }
}
