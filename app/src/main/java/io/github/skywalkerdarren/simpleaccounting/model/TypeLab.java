package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.github.skywalkerdarren.simpleaccounting.model.TypeDbSchema.TypeTable.Cols;

/**
 * @author darren
 * @date 2018/3/28
 */

public class TypeLab {
    private static TypeLab sTypeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TypeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TypeBaseHelper(mContext).getWritableDatabase();
    }

    public static TypeLab getInstance(Context context) {
        if (sTypeLab == null) {
            return new TypeLab(context);
        } else {
            return sTypeLab;
        }
    }

    private static ContentValues getContentValues(Type type) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, type.getId().toString());
        values.put(Cols.NAME, type.getName());
        values.put(Cols.COLOR, type.getColorId());
        values.put(Cols.RES_ID, type.getTypeId());
        values.put(Cols.IS_EXPENSE, type.getExpense());
        return values;
    }
}
