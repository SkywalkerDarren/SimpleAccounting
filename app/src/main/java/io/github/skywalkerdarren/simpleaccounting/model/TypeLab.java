package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.TypeTable.Cols;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

/**
 * @author darren
 * @date 2018/3/28
 */

public class TypeLab {
    private static TypeLab sTypeLab;
    private SQLiteDatabase mDatabase;

    private TypeLab(Context context) {
        mDatabase = new DbBaseHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public static TypeLab getInstance(Context context) {
        if (sTypeLab == null) {
            return sTypeLab = new TypeLab(context);
        } else {
            return sTypeLab;
        }
    }

    private static ContentValues getContentValues(Type type) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, type.getUUID().toString());
        values.put(Cols.NAME, type.getName());
        values.put(Cols.COLOR, type.getColorId());
        values.put(Cols.IMAGE, type.getAssetsName());
        values.put(Cols.IS_EXPENSE, type.getIsExpense() ? 1 : 0);
        return values;
    }

    /**
     * 设置初始数据
     */
    static void initTypeDb(SQLiteDatabase sqLiteDatabase) {
        List<Type> types = new ArrayList<>(10);
        // 支出类型

        for (Type type : types) {
            sqLiteDatabase.insert(DbSchema.TypeTable.TABLE_NAME, null,
                    getContentValues(type));
        }
    }

    private TypeCursorWrapper queryTypes(String where, String[] args) {
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(DbSchema.TypeTable.TABLE_NAME,
                null,
                where,
                args,
                null,
                null,
                null);
        return new TypeCursorWrapper(cursor);
    }

    public Type getType(UUID uuid) {
        try (TypeCursorWrapper cursor = queryTypes(Cols.UUID + " == ?",
                new String[]{uuid.toString()})) {
            cursor.moveToFirst();
            return cursor.getType();
        }
    }

    /**
     * 获取类型集合
     *
     * @param isExpense true为支出类型
     * @return 类型集
     */
    public List<Type> getTypes(boolean isExpense) {
        List<Type> types = new ArrayList<>(10);
        try (TypeCursorWrapper cursor = queryTypes(Cols.IS_EXPENSE + "== ?",
                new String[]{isExpense ? "1" : "0"})) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                types.add(cursor.getType());
                cursor.moveToNext();
            }
        }
        return types;
    }

    /**
     * 删除类型，及其相关账单
     *
     * @param uuid 类型id
     */
    public void delType(UUID uuid) {
        mDatabase.delete(DbSchema.TypeTable.TABLE_NAME,
                Cols.UUID + " = ?",
                new String[]{uuid.toString()});
        mDatabase.delete(DbSchema.TypeTable.TABLE_NAME,
                DbSchema.BillTable.Cols.TYPE_ID + " = ?",
                new String[]{uuid.toString()});
    }
}
