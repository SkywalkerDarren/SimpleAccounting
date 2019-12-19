package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author darren
 * @date 2018/3/31
 */

public class StatsLab {

    private static StatsLab sStatsLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * 构造统计库
     * 只读
     *
     * @param context 应用上下文
     */
    private StatsLab(Context context) {
        // 数据库方式
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getReadableDatabase();
    }

    /**
     * 单例
     * 获取统计数据库
     *
     * @param context 应用上下文
     * @return stats库
     */
    public static StatsLab getInstance(Context context) {
        if (sStatsLab == null) {
            return sStatsLab = new StatsLab(context);
        } else {
            return sStatsLab;
        }
    }


    /**
     * 年度统计
     *
     * @param year 年份
     * @return 统计列表
     */
    public List<BillStats> getAnnualStats(int year) {
        List<BillStats> statsList = new ArrayList<>(12);
        final int month = 12;
        for (int i = 1; i <= month; i++) {
            DateTime start = new DateTime(year, i, 1, 0, 0);
            DateTime end = start.plusMonths(1);
            statsList.add(getStats(start, end));
        }
        return statsList;
    }


    /**
     * 月度统计
     *
     * @param year  年份
     * @param month 月份
     * @return 统计列表
     */
    public List<BillStats> getMonthStats(int year, int month) {
        List<BillStats> statsList = new ArrayList<>(12);
        DateTime dateTime = new DateTime(year, month, 1, 0, 0);
        int days = dateTime.plusMonths(1).minus(1L).getDayOfMonth();
        for (int i = 1; i <= days; i++) {
            DateTime start = new DateTime(year, month, i, 0, 0);
            DateTime end = start.plusDays(1);
            statsList.add(getStats(start, end));
        }
        return statsList;
    }

    /**
     * 按类型从高到低的获得每个类型的统计数据
     *
     * @param start     起始日期
     * @param end       结束日期
     * @param isExpense 是否为支出
     * @return 统计数据
     */
    public List<TypeStats> getTypeStats(DateTime start, DateTime end, boolean isExpense) {
        TypeLab lab = TypeLab.getInstance(mContext);
        List<TypeStats> typeStats = new ArrayList<>(10);
        try (CursorWrapper cursor = getTypesInfoCursor(start, end)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // 判断收支类型
                if ((isExpense ? "1" : "0").equals(cursor.getString(1))) {
                    UUID typeId = UUID.fromString(cursor.getString(0));
                    BigDecimal sum = new BigDecimal(cursor.getString(2));
                    typeStats.add(new TypeStats(lab.getType(typeId), sum));
                }
                cursor.moveToNext();
            }
        }
        if (typeStats.size() < 1) {
            return null;
        }
        return typeStats;
    }

    /**
     * 类型的统计数据
     *
     * @param start  起始日期
     * @param end    结束日期
     * @param typeId 类型id
     * @return 统计数据, null则找不到
     */
    public BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId) {
        try (CursorWrapper cursor = getTypeCursor(typeId, start, end)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return new BigDecimal(cursor.getString(0));
            }
        }
        return null;
    }

    /**
     * 该类型的金额总额
     */
    private CursorWrapper getTypeCursor(UUID typeId, DateTime start, DateTime end) {
        return queryStats(new String[]{"sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.TypeTable.Cols.UUID + " = ? and " +
                        DbSchema.BillTable.Cols.DATE + " between ? and ?",
                new String[]{typeId.toString(), start.getMillis() + "", end.getMillis() + ""},
                null, null);
    }


    /**
     * 获取一年内一个账户的收支统计信息
     *
     * @param accountId 账户id
     * @return 统计信息
     */
    public List<AccountStats> getAccountStats(UUID accountId, int year) {
        List<AccountStats> stats = new ArrayList<>();
        final int month = 12;
        DateTime dateTime = new DateTime(year, 1, 1, 0,
                0, 0);
        for (int m = 0; m < month; m++) {
            dateTime = dateTime.plusMonths(1);
            stats.add(getAccountStats(accountId, dateTime, dateTime.plusMonths(1)));
        }
        return stats;
    }

    /**
     * 一段时间内的账户统计
     *
     * @param accountId 账户id
     * @param start     起始时间
     * @param end       结束时间
     * @return 统计结果
     */
    public AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        try (CursorWrapper cursor = getAccountCursor(start, end, accountId)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if ("1".equals(cursor.getString(0))) {
                    expense = new BigDecimal(cursor.getString(1));
                } else {
                    income = new BigDecimal(cursor.getString(1));
                }
                cursor.moveToNext();
            }
        }
        return new AccountStats(income, expense);
    }

    private CursorWrapper getAccountCursor(DateTime start, DateTime end, UUID accountId) {
        return new CursorWrapper(queryStats(
                new String[]{DbSchema.TypeTable.Cols.IS_EXPENSE,
                        "sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " between ? and ? and " +
                        "uuid" + " = ?",
                new String[]{start.getMillis() + "", end.getMillis() + "", accountId.toString()},
                DbSchema.TypeTable.Cols.IS_EXPENSE,
                null
        ));
    }

    /**
     * 一段时间的账单结算统计
     *
     * @param start 起始时间
     * @param end   结束时间
     * @return 包括支出，收入，盈余的统计结果
     */
    public BillStats getStats(DateTime start, DateTime end) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        try (CursorWrapper cursor = getBillsInfoCursor(start, end)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if ("0".equals(cursor.getString(0))) {
                    income = new BigDecimal(cursor.getString(1));
                } else {
                    expense = new BigDecimal(cursor.getString(1));
                }
                cursor.moveToNext();
            }
        }
        return new BillStats(income, expense);
    }

    /**
     * 统计收支总额的游标
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 游标
     */
    @NonNull
    private CursorWrapper getBillsInfoCursor(DateTime start, DateTime end) {
        return queryStats(
                new String[]{DbSchema.TypeTable.Cols.IS_EXPENSE, "sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis())},
                DbSchema.TypeTable.Cols.IS_EXPENSE,
                DbSchema.BillTable.Cols.DATE + " DESC"
        );
    }

    /**
     * 统计每个支出或收入种类的求和游标，从高到低排列
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 游标
     */
    @NonNull
    private CursorWrapper getTypesInfoCursor(DateTime start, DateTime end) {
        return queryStats(
                new String[]{DbSchema.TypeTable.Cols.UUID, DbSchema.TypeTable.Cols.IS_EXPENSE,
                        "sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis())},
                DbSchema.TypeTable.Cols.NAME,
                "sum(" + DbSchema.BillTable.Cols.BALANCE + ") DESC"
        );
    }

    /**
     * 类型的平均值
     */
    public BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId) {
        try (Cursor cursor = getTypeAvgCursorWrapper(start, end, typeId)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return new BigDecimal(cursor.getString(0));
            }
        }
        return null;
    }

    @NonNull
    private CursorWrapper getTypeAvgCursorWrapper(DateTime start, DateTime end, UUID typeId) {
        return queryStats(new String[]{"avg(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.TypeTable.Cols.UUID + " = ? and " +
                        DbSchema.BillTable.Cols.DATE + " between ? and ?",
                new String[]{typeId.toString(), start.getMillis() + "", end.getMillis() + ""},
                null, null);
    }
}
