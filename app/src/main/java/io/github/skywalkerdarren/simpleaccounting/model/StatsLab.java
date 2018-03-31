package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private StatsCursorWrapper queryStats(String[] cols, String where, String[] args,
                                          String group, String order) {
        return new StatsCursorWrapper(mDatabase
                .query(DbSchema.BillTable.NAME + ", " + DbSchema.TypeTable.NAME,
                        cols,
                        where + " and " +
                                DbSchema.BillTable.Cols.TYPE_ID + " == " + DbSchema.TypeTable.Cols.UUID,
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
    public List<Stats> getAnnualStats(int year) {
        List<Stats> statsList = new ArrayList<>(12);
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
    public List<Stats> getMonthStats(int year, int month) {
        List<Stats> statsList = new ArrayList<>(12);
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
        List<Type> types = TypeLab.getInstance(mContext).getTypes(isExpense);
        List<TypeStats> typeStats = new ArrayList<>(10);
        try (StatsCursorWrapper cursor = getTypesInfoCursor(start, end, isExpense ? "1" : "0")) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(0);
                int i = 0;
                for (; i < types.size(); i++) {
                    if (name.equals(types.get(i).getName())) {
                        break;
                    }
                }
                typeStats.add(new TypeStats(types.get(i),
                        new BigDecimal(cursor.getString(1))));
                cursor.moveToNext();
            }
        }
        if (typeStats.size() < 1) {
            return null;
        }
        return typeStats;
    }

    /**
     * 一段时间的账单结算统计
     *
     * @param start 起始时间
     * @param end   结束时间
     * @return 包括支出，收入，盈余的统计结果
     */
    public Stats getStats(DateTime start, DateTime end) {
        final String isExpense = "1";
        final String isIncome = "0";
        String income = getStats(start, end, isIncome);
        String expense = getStats(start, end, isExpense);

        return new Stats(new BigDecimal(income), new BigDecimal(expense));
    }

    @NonNull
    private String getStats(DateTime start, DateTime end, String isExpense) {
        String balance;
        try (StatsCursorWrapper cursor = getBillsInfoCursor(start, end, isExpense)) {
            cursor.moveToFirst();
            String num = cursor.getString(0);
            if (num == null) {
                num = "0";
            }
            balance = num;
        }
        return balance;
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
    private StatsCursorWrapper getBillsInfoCursor(DateTime start, DateTime end, String s) {
        return queryStats(
                new String[]{"sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " BETWEEN ? AND ? and " +
                        DbSchema.TypeTable.Cols.IS_EXPENSE + " == ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis()), s},
                null,
                DbSchema.BillTable.Cols.DATE + " DESC"
        );
    }

    /**
     * 统计每个支出或收入种类的求和游标，从高到低排列
     *
     * @param start 开始日期
     * @param end   结束日期
     * @param s     "1"是支出类型 "0"是收入类型
     * @return 游标
     */
    @NonNull
    private StatsCursorWrapper getTypesInfoCursor(DateTime start, DateTime end, String s) {
        return queryStats(
                new String[]{DbSchema.TypeTable.Cols.NAME, "sum(" + DbSchema.BillTable.Cols.BALANCE + ")"},
                DbSchema.BillTable.Cols.DATE + " BETWEEN ? AND ? and " +
                        DbSchema.TypeTable.Cols.IS_EXPENSE + " == ?",
                new String[]{String.valueOf(start.getMillis()), String.valueOf(end.getMillis()), s},
                DbSchema.TypeTable.Cols.NAME,
                "sum(" + DbSchema.BillTable.Cols.BALANCE + ") DESC"
        );
    }

    /**
     * 类型统计类
     */
    public class TypeStats {
        private Type mType;
        private BigDecimal sum;

        TypeStats(Type type, BigDecimal sum) {
            mType = type;
            this.sum = sum;
        }

        public Type getType() {
            return mType;
        }

        public BigDecimal getSum() {
            return sum;
        }
    }

    /**
     * 统计结果类
     */
    public class Stats {

        private BigDecimal income;
        private BigDecimal expense;
        private BigDecimal sum;

        Stats(BigDecimal income, BigDecimal expense) {
            this.income = income;
            this.expense = expense;
            sum = income.subtract(expense);
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
}
