package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

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

    @SuppressLint("StaticFieldLeak")
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
     * 三表联表查询
     */
    private CursorWrapper queryStats(String[] cols, String where, String[] args,
                                     String group, String order) {
        return new CursorWrapper(mDatabase
                .query(DbSchema.BillTable.TABLE_NAME + " inner join " +
                                DbSchema.TypeTable.TABLE_NAME + " inner join " +
                                DbSchema.AccountTable.TABLE_NAME + " on " +
                                DbSchema.BillTable.Cols.ACCOUNT_ID + " = " +
                                DbSchema.AccountTable.Cols.UUID + " and " +
                                DbSchema.BillTable.Cols.TYPE_ID + " = " +
                                DbSchema.TypeTable.Cols.UUID,
                        cols,
                        where,
                        args,
                        group,
                        null,
                        order));
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
     * 获取一年内一个账户的收支统计信息
     *
     * @param accountId 账户id
     * @return 统计信息
     */
    public List<AccountStats> getAccountStats(UUID accountId, int year) {
        List<AccountStats> stats = new ArrayList<>();
        final int month = 12;
        DateTime dateTime = new DateTime(year, 1, 1, 0, 0, 0);
        for (int m = 0; m < month; m++) {
            dateTime = dateTime.plusMonths(1);
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;
            try (CursorWrapper cursor = getAccountCursor(dateTime, dateTime.plusMonths(1), accountId)) {
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
            stats.add(new AccountStats(income, expense));
        }
        return stats;
    }

    private CursorWrapper getAccountCursor(DateTime start, DateTime end, UUID accountId) {
        return new CursorWrapper(queryStats(
                new String[]{DbSchema.TypeTable.Cols.IS_EXPENSE, "sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " between ? and ? and " +
                        DbSchema.AccountTable.Cols.UUID + " = ?",
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
                if ("1".equals(cursor.getString(0))) {
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
     * 类型统计类
     */
    public class TypeStats extends Stats {
        private Type mType;

        TypeStats(Type type, BigDecimal balance) {
            super(balance);
            mType = type;
        }

        public Type getType() {
            return mType;
        }

        @Deprecated
        @Override
        public BigDecimal getIncome() {
            return getSum();
        }

        @Deprecated
        @Override
        public BigDecimal getExpense() {
            return getSum();
        }
    }

    /**
     * 账单统计结果类
     */
    public class BillStats extends Stats {
        BillStats(BigDecimal income, BigDecimal expense) {
            super(income, expense);
        }
    }


    public abstract class Stats {
        private BigDecimal income;
        private BigDecimal expense;
        private BigDecimal sum;

        Stats(BigDecimal income, BigDecimal expense) {
            this.income = income;
            this.expense = expense;
            sum = income.subtract(expense);
        }

        Stats(BigDecimal sum) {
            income = BigDecimal.ZERO;
            expense = BigDecimal.ZERO;
            this.sum = sum;
        }

        public BigDecimal getIncome() {
            return income;
        }

        public BigDecimal getExpense() {
            return expense;
        }

        public BigDecimal getSum() {
            return sum;
        }
    }

    /**
     * 账户账单统计
     */
    public class AccountStats extends Stats {
        AccountStats(BigDecimal income, BigDecimal expense) {
            super(income, expense);
        }
    }
}
