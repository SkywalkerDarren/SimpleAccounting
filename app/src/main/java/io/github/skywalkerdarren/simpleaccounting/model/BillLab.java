package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static io.github.skywalkerdarren.simpleaccounting.model.BillDbSchema.BillTable;

/**
 * Created by darren on 2018/1/29.
 * 账单库类
 * 从库中获取特定账单
 * 前期使用特定临时账单
 * 后期用数据库持久化代替
 */

public class BillLab {
    private static BillLab sBillLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
    public static final String SUM = "sum";

    /**
     * 单例
     * 获取账单数据库
     *
     * @param context 应用上下文
     * @return bill库
     */
    public static BillLab getInstance(Context context) {
        if (sBillLab == null) {
            return sBillLab = new BillLab(context);
        } else {
            return sBillLab;
        }
    }

    private BillLab(Context context) {
        // 数据库方式
        mContext = context.getApplicationContext();
        mDatabase = new BillBaseHelper(context).getWritableDatabase();

        // 临时生成0个账单
        for (int i = 0; i < 0; i++) {
            Bill bill = createRandomBill(i);
            addBill(bill);
        }
    }

    /**
     * 随机创建一个账单
     *
     * @param i 编号
     * @return 账单
     */
    @NonNull
    public static Bill createRandomBill(int i) {
        Bill bill = new Bill();
        if (new Random().nextInt(2) > 0) {
            bill.setRemark("这是备注：#" + i);
        } else {
            bill.setRemark("");
        }
        bill.setBalance(new BigDecimal(new Random().nextDouble() * 1000).setScale(2, BigDecimal.ROUND_HALF_UP));
        bill.setDate(DateTime.now());

        // 1/5的几率为收入类型
        if (new Random().nextInt(5) < 1) {
            int position = new Random().nextInt(IncomeType.getValues().length);
            String type = IncomeType.getValues()[position];
            bill.setExpense(IncomeType.getInstance());
            bill.setType(type);
            bill.setName("这是" + type + "：#" + i);
        } else {
            int position = new Random().nextInt(ExpenseType.getValues().length);
            String type = ExpenseType.getValues()[position];
            bill.setExpense(ExpenseType.getInstance());

            bill.setType(type);
            bill.setName("这是" + type + "：#" + i);
        }
        return bill;
    }

    /**
     * 根据账单生成用于存储进数据库的键值对
     *
     * @param bill 账单
     * @return 内容数据
     */
    private static ContentValues getContentValues(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(BillTable.Cols.UUID, bill.getId().toString());
        values.put(BillTable.Cols.BALANCE, bill.getBalance().toString());
        values.put(BillTable.Cols.DATE, bill.getDate().getMillis());
        values.put(BillTable.Cols.IS_EXPENSE, bill.isExpense() ? 1 : 0);
        values.put(BillTable.Cols.NAME, bill.getName());
        values.put(BillTable.Cols.REMARK, bill.getRemark());
        values.put(BillTable.Cols.TYPE, bill.getType());
        return values;
    }

    /**
     * 账单按日期顺序查询游标
     *
     * @param whereClause where子句
     * @param whereArgs   查询参数
     * @return 游标
     */
    private BillCursorWrapper queryBills(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BillTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                BillTable.Cols.DATE + " DESC"
        );
        return new BillCursorWrapper(cursor);
    }

    /**
     * 账单查询游标
     *
     * @param whereClause where子句
     * @param whereArgs   查询参数
     * @return 游标
     */
    private BillCursorWrapper queryBills(String[] columns, String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                BillTable.NAME,
                columns,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );
        return new BillCursorWrapper(cursor);
    }

    private BillCursorWrapper rawQueryBills(String sql, String[] whereArgs) {
        Cursor cursor = mDatabase.rawQuery(sql, whereArgs);
        return new BillCursorWrapper(cursor);
    }

    /**
     * 根据账单id从数据库中找到bill
     *
     * @param id 账单id
     * @return 对应bill
     */
    public Bill getBill(UUID id) {
        BillCursorWrapper cursor = queryBills(BillTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        Bill bill;
        try {
            cursor.moveToFirst();
            bill = cursor.getBill();
        } finally {
            cursor.close();
        }
        return bill;
    }

    /**
     * @return 对应年月所有账单
     */
    public List<Bill> getsBills(int year, int month) {
        List<Bill> bills = new ArrayList<>(100);
        DateTime start = new DateTime(year, month, 1, 0, 0);
        DateTime end = start.plusMonths(1);

        BillCursorWrapper billCursorWrapper = queryBills(BillTable.Cols.DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis())});
        try {
            billCursorWrapper.moveToFirst();
            while (!billCursorWrapper.isAfterLast()) {
                bills.add(billCursorWrapper.getBill());
                billCursorWrapper.moveToNext();
            }
        } finally {
            billCursorWrapper.close();
        }
        return bills;
    }


    /**
     * 日结算统计
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 统计表
     */
    public Map<String, BigDecimal> getDayStatics(int year, int month, int day) {
        final String isExpense = "1";
        final String isIncome = "0";
        Map<String, BigDecimal> statics = new HashMap<>(7);
        DateTime start = new DateTime(year, month, day, 0, 0);
        DateTime end = start.plusDays(1);
        BillCursorWrapper cursor = null;
        try {
            cursor = getBillsInfoCursor(start, end, isExpense);
            cursor.moveToFirst();
            String num = cursor.getString(0);
            if (num == null) {
                num = "0";
            }
            statics.put(EXPENSE, new BigDecimal(num));
            cursor = getBillsInfoCursor(start, end, isIncome);
            cursor.moveToFirst();
            num = cursor.getString(0);
            if (num == null) {
                num = "0";
            }
            statics.put(INCOME, new BigDecimal(num));
            statics.put(SUM, statics.get(INCOME).subtract(statics.get(EXPENSE)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return statics;
    }

    /**
     * 统计收入或支出总额的游标
     *
     * @param start 开始日期
     * @param end   结束日期
     * @param s     "1"是支出类型 "0"是收入类型
     * @return 游标
     */
    @NonNull
    private BillCursorWrapper getBillsInfoCursor(DateTime start, DateTime end, String s) {
        return queryBills(
                new String[]{"sum(" + BillTable.Cols.BALANCE + ")"},
                BillTable.Cols.DATE + " BETWEEN ? AND ? and " + BillTable.Cols.IS_EXPENSE + " == ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis()), s},
                BillTable.Cols.DATE + " DESC"
        );
    }

    /**
     * 增加一个账单到数据库
     *
     * @param bill 账单
     */
    public void addBill(Bill bill) {
        ContentValues values = getContentValues(bill);
        mDatabase.insert(BillTable.NAME, null, values);
    }

    /**
     * 根据id删除对应账单
     *
     * @param id 账单id
     */
    public void delBill(UUID id) {
        mDatabase.delete(BillTable.NAME,
                BillTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
    }
}
