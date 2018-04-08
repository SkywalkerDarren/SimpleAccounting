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
        static final String TABLE_NAME = "bill";

        /**
         * 列名
         */
        static final class Cols {
            static final String UUID = TABLE_NAME + "_uuid";
            static final String NAME = TABLE_NAME + "_name";
            static final String BALANCE = TABLE_NAME + "_balance";
            static final String TYPE_ID = TABLE_NAME + "_type_id";
            static final String DATE = TABLE_NAME + "_date";
            static final String REMARK = TABLE_NAME + "_remark";
            static final String ACCOUNT_ID = TABLE_NAME + "_account_id";
        }
    }

    /**
     * 类型表
     */
    static final class TypeTable {

        /**
         * 表名
         */
        static final String TABLE_NAME = "type";

        /**
         * 列名
         */
        static final class Cols {
            static final String UUID = TABLE_NAME + "_uuid";
            static final String NAME = TABLE_NAME + "_name";
            static final String RES_ID = TABLE_NAME + "_res_id";
            static final String COLOR = TABLE_NAME + "_color";
            static final String IS_EXPENSE = TABLE_NAME + "_is_expense";
        }
    }

    /**
     * 账户表
     */
    static final class AccountTable {

        /**
         * 表名
         */
        static final String TABLE_NAME = "account";

        /**
         * 列名
         */
        static final class Cols {
            static final String UUID = TABLE_NAME + "_uuid";
            static final String NAME = TABLE_NAME + "_name";
            static final String IMAGE_ID = TABLE_NAME + "_image_id";
            static final String COLOR_ID = TABLE_NAME + "_color_id";
            static final String BALANCE = TABLE_NAME + "_balance";
            static final String BALANCE_HINT = TABLE_NAME + "_balance_hint";
        }
    }
}
