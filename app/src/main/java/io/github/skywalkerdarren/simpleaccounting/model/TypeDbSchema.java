package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * @author darren
 * @date 2018/3/31
 */

public class TypeDbSchema {
    public static final class TypeTable {
        public static final String NAME = "type";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String RES_ID = "res_id";
            public static final String COLOR = "color";
            public static final String IS_EXPENSE = "is_expense";
        }
    }
}
