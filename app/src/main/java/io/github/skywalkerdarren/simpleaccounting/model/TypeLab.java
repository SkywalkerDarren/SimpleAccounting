package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.TypeTable.Cols;

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
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public static TypeLab getInstance(Context context) {
        if (sTypeLab == null) {
            return new TypeLab(context);
        } else {
            return sTypeLab;
        }
    }

    private TypeCursorWrapper queryTypes(String where, String[] args) {
        Cursor cursor = mDatabase.query(DbSchema.TypeTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null);
        return new TypeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Type type) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, type.getId().toString());
        values.put(Cols.NAME, type.getName());
        values.put(Cols.COLOR, type.getColorId());
        values.put(Cols.RES_ID, type.getTypeId());
        values.put(Cols.IS_EXPENSE, type.getExpense() ? 1 : 0);
        return values;
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
     * 设置初始数据
     */
    static void initTypeDb(SQLiteDatabase sqLiteDatabase) {
        List<Type> types = new ArrayList<>(10);

        // 支出类型
        types.add(new Type().setName("吃喝").setExpense(true)
                .setResId(R.drawable.type_image_diet)
                .setColorId(Color.rgb(0xe6, 0xc4, 0x53)));
        types.add(new Type().setName("娱乐").setExpense(true)
                .setResId(R.drawable.type_image_entertainment)
                .setColorId(Color.rgb(0x73, 0xc8, 0xd5)));
        types.add(new Type().setName("交通").setExpense(true)
                .setResId(R.drawable.type_image_traffic)
                .setColorId(Color.rgb(0xf9, 0xd5, 0x5d)));
        types.add(new Type().setName("日用品").setExpense(true)
                .setResId(R.drawable.type_image_daily_necessities)
                .setColorId(Color.rgb(0xf0, 0x8d, 0x78)));
        types.add(new Type().setName("化妆护肤").setExpense(true)
                .setResId(R.drawable.type_image_make_up)
                .setColorId(Color.rgb(0xfe, 0x4c, 0x5e)));
        types.add(new Type().setName("医疗").setExpense(true)
                .setResId(R.drawable.type_image_medical)
                .setColorId(Color.rgb(0xc0, 0xf1, 0xf9)));
        types.add(new Type().setName("服饰").setExpense(true)
                .setResId(R.drawable.type_image_apparel)
                .setColorId(Color.rgb(0x7e, 0xb7, 0x9f)));
        types.add(new Type().setName("话费").setExpense(true)
                .setResId(R.drawable.type_image_calls)
                .setColorId(Color.rgb(0x83, 0x6c, 0xab)));
        types.add(new Type().setName("红包").setExpense(true)
                .setResId(R.drawable.type_image_red_package)
                .setColorId(Color.rgb(0xf7, 0x2e, 0x42)));
        types.add(new Type().setName("其他").setExpense(true)
                .setResId(R.drawable.type_image_other)
                .setColorId(Color.rgb(0xcd, 0x53, 0x3b)));

        // 收入类型
        types.add(new Type().setName("工资").setExpense(false)
                .setResId(R.drawable.type_image_wage)
                .setColorId(Color.rgb(0x97, 0x73, 0x69)));
        types.add(new Type().setName("兼职").setExpense(false)
                .setResId(R.drawable.type_image_part_time)
                .setColorId(Color.rgb(0xa7, 0xee, 0xf9)));
        types.add(new Type().setName("奖金").setExpense(false)
                .setResId(R.drawable.type_image_prize)
                .setColorId(Color.rgb(0xf4, 0xbc, 0xb1)));
        types.add(new Type().setName("理财投资").setExpense(false)
                .setResId(R.drawable.type_image_invest)
                .setColorId(Color.rgb(0xff, 0xec, 0xab)));
        types.add(new Type().setName("红包").setExpense(false)
                .setResId(R.drawable.type_image_red_package)
                .setColorId(Color.rgb(0xf7, 0x2e, 0x42)));
        types.add(new Type().setName("其他").setExpense(false)
                .setResId(R.drawable.type_image_other)
                .setColorId(Color.rgb(0xcd, 0x53, 0x3b)));

        for (Type type : types) {
            sqLiteDatabase.insert(DbSchema.TypeTable.NAME, null,
                    getContentValues(type));
        }
    }
}
