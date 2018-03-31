package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author darren
 * @date 2018/3/28
 */

public class TypeBaseHelper extends SQLiteOpenHelper {
    /**
     * 数据库版本号
     */
    private static final int VERSION = 1;

    /**
     * 数据库名字
     */
    private static final String DATABASE_NAME = "Types.db";

    public TypeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + TypeDbSchema.TypeTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        TypeDbSchema.TypeTable.Cols.UUID + ", " +
                        TypeDbSchema.TypeTable.Cols.NAME + ", " +
                        TypeDbSchema.TypeTable.Cols.IS_EXPENSE + " INT , " +
                        TypeDbSchema.TypeTable.Cols.RES_ID + ", " +
                        TypeDbSchema.TypeTable.Cols.COLOR +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
