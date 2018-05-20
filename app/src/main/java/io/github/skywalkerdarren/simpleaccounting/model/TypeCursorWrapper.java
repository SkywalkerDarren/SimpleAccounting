package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.TypeTable.Cols;


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
        byte[] bytes = getBlob(getColumnIndex(Cols.RES));
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return new Type(id)
                .setName(getString(getColumnIndex(Cols.NAME)))
                .setBitmap(bitmap)
                .setColorId(getInt(getColumnIndex(Cols.COLOR)))
                .setExpense(getInt(getColumnIndex(Cols.IS_EXPENSE)) == 1);
    }
}
