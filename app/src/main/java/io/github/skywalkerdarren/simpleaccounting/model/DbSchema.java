package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * 定义账单表
 *
 * @author darren
 * @date 2018/1/30
 */

class DbSchema {
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
            public static final String UUID = "bill_id";
            public static final String NAME = "bill_name";
            public static final String BALANCE = "balance";
            public static final String TYPE_ID = "type_id";
            public static final String DATE = "date";
            public static final String REMARK = "remark";
        }
    }

    /**
     * 类型表
     */
    public static final class TypeTable {

        /**
         * 表名
         */
        public static final String NAME = "type";

        /**
         * 列名
         */
        public static final class Cols {
            public static final String UUID = "type_uuid";
            public static final String NAME = "type_name";
            public static final String RES_ID = "res_id";
            public static final String COLOR = "color";
            public static final String IS_EXPENSE = "is_expense";
        }
    }
}
