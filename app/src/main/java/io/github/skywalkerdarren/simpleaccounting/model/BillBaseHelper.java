package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by darren on 2018/1/30.
 * 账单表数据库辅助类
 */

public class BillBaseHelper extends SQLiteOpenHelper {
    /**
     * 数据库版本号
     */
    private static final int VERSION = 1;
    /**
     * 数据库名字
     */
    private static final String DATABASE_NAME = "Bills.db";

    public BillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + BillDbSchema.BillTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        BillDbSchema.BillTable.Cols.UUID + ", " +
                        BillDbSchema.BillTable.Cols.BALANCE + ", " +
                        BillDbSchema.BillTable.Cols.IS_EXPENSE + " INT , " +
                        BillDbSchema.BillTable.Cols.DATE + " LONG , " +
                        BillDbSchema.BillTable.Cols.NAME + ", " +
                        BillDbSchema.BillTable.Cols.REMARK + ", " +
                        BillDbSchema.BillTable.Cols.TYPE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
