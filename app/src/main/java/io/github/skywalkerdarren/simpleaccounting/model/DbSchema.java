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
    static final class BillTable {
        /**
         * 表名
         */
        static final String NAME = "bill";

        /**
         * 列名
         */
        static final class Cols {
            static final String UUID = "bill_id";
            static final String NAME = "bill_name";
            static final String BALANCE = "balance";
            static final String TYPE_ID = "type_id";
            static final String DATE = "date";
            static final String REMARK = "remark";
        }
    }

    /**
     * 类型表
     */
    static final class TypeTable {

        /**
         * 表名
         */
        static final String NAME = "type";

        /**
         * 列名
         */
        static final class Cols {
            static final String UUID = "type_uuid";
            static final String NAME = "type_name";
            static final String RES_ID = "res_id";
            static final String COLOR = "color";
            static final String IS_EXPENSE = "is_expense";
        }
    }
}
