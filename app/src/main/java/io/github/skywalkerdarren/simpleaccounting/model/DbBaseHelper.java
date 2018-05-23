package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static io.github.skywalkerdarren.simpleaccounting.model.AccountLab.initAccountDb;
import static io.github.skywalkerdarren.simpleaccounting.model.TypeLab.initTypeDb;

/**
 * 账单表数据库辅助类
 *
 * @author darren
 * @date 2018/1/30
 */

class DbBaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库版本号
     */
    private static final int VERSION = 1;

    /**
     * 数据库名字
     */
    private static final String DATABASE_NAME = "Bills.db";

    /**
     * 构造bill账单游标
     *
     * @param context 应用上下文
     */
    DbBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建账单表
        sqLiteDatabase.execSQL(
                "create table " + DbSchema.BillTable.TABLE_NAME + "( " +
                        DbSchema.BillTable.TABLE_NAME +
                        "_id integer primary key autoincrement, " +
                        DbSchema.BillTable.Cols.UUID + ", " +
                        DbSchema.BillTable.Cols.NAME + ", " +
                        DbSchema.BillTable.Cols.BALANCE + ", " +
                        DbSchema.BillTable.Cols.DATE + " LONG , " +
                        DbSchema.BillTable.Cols.REMARK + ", " +
                        DbSchema.BillTable.Cols.TYPE_ID + ", " +
                        DbSchema.BillTable.Cols.ACCOUNT_ID +
                        ")"
        );

        // 创建类型表
        sqLiteDatabase.execSQL(
                "create table " + DbSchema.TypeTable.TABLE_NAME + "( " +
                        DbSchema.TypeTable.TABLE_NAME +
                        "_id integer primary key autoincrement, " +
                        DbSchema.TypeTable.Cols.UUID + ", " +
                        DbSchema.TypeTable.Cols.NAME + ", " +
                        DbSchema.TypeTable.Cols.IS_EXPENSE + " INT , " +
                        DbSchema.TypeTable.Cols.IMAGE + " VARCHAR(32) , " +
                        DbSchema.TypeTable.Cols.COLOR +
                        ")"
        );
        initTypeDb(sqLiteDatabase);

        // 创建账户表
        sqLiteDatabase.execSQL(
                "create table " + DbSchema.AccountTable.TABLE_NAME + "( " +
                        DbSchema.AccountTable.TABLE_NAME +
                        "_id integer primary key autoincrement, " +
                        DbSchema.AccountTable.Cols.UUID + ", " +
                        DbSchema.AccountTable.Cols.NAME + ", " +
                        DbSchema.AccountTable.Cols.BALANCE + " , " +
                        DbSchema.AccountTable.Cols.BALANCE_HINT + " , " +
                        DbSchema.AccountTable.Cols.IMAGE + " VARCHAR(32) , " +
                        DbSchema.AccountTable.Cols.COLOR_ID +
                        ")"
        );
        initAccountDb(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
