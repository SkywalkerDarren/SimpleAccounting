package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * Created by darren on 2018/1/30.
 * 定义账单表
 */

public class BillDbSchema {
    /**
     * 账单表
     */
    public static final class BillTable {
        /**
         * 表名
         */
        public static final String NAME = "bill";

        /**
         * 列名
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String BALANCE = "balance";
            public static final String IS_EXPENSE = "isExpense";
            public static final String TYPE = "type";
            public static final String DATE = "date";
            public static final String REMARK = "remark";
        }
    }
}
