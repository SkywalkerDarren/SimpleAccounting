package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.skywalkerdarren.simpleaccounting.model.DbSchema.BillTable;

/**
 * 账单库类
 * 从库中获取特定账单
 * 前期使用特定临时账单
 * 后期用数据库持久化代替
 *
 * @author darren
 * @date 2018/1/29
 */

public class BillLab {
    private static BillLab sBillLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * 构造账单库
     *
     * @param context 应用上下文
     */
    private BillLab(Context context) {
        // 数据库方式
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(context).getWritableDatabase();
    }

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
        values.put(BillTable.Cols.NAME, bill.getName());
        values.put(BillTable.Cols.REMARK, bill.getRemark());
        values.put(BillTable.Cols.TYPE_ID, bill.getTypeId().toString());
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
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(
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
     * 根据账单id从数据库中找到bill
     *
     * @param id 账单id
     * @return 对应bill
     */
    public Bill getBill(UUID id) {
        Bill bill;
        try (BillCursorWrapper cursor = queryBills(BillTable.Cols.UUID + " = ?",
                new String[]{id.toString()})) {
            cursor.moveToFirst();
            bill = cursor.getBill();
        } catch (CursorIndexOutOfBoundsException e) {
            bill = null;
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

        try (BillCursorWrapper billCursorWrapper = queryBills(BillTable.Cols.DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis())})) {
            billCursorWrapper.moveToFirst();
            while (!billCursorWrapper.isAfterLast()) {
                bills.add(billCursorWrapper.getBill());
                billCursorWrapper.moveToNext();
            }
        }
        return bills;
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

    /**
     * 更新账单
     *
     * @param bill 账单
     */
    public void updateBill(Bill bill) {
        ContentValues values = getContentValues(bill);
        mDatabase.update(BillTable.NAME, values, BillTable.Cols.UUID + " = ?",
                new String[]{bill.getId().toString()});
    }
}
