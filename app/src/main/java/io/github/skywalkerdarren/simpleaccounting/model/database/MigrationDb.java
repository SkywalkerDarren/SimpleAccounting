package io.github.skywalkerdarren.simpleaccounting.model.database;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationDb {
    /**
     * 新数据库改动太大了
     * 反正没什么人用
     * 数据库重建算了
     */
    @Deprecated
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `currency` ( \n" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "`source` TEXT, \n" +
                    "`name` TEXT UNIQUE, \n" +
                    "`exchange_rate` REAL, \n" +
                    "`favourite` INTEGER \n" +
                    ")");
            database.execSQL("CREATE INDEX index_currency_favourite \n" +
                    "ON currency (`favourite`)");
            database.execSQL("CREATE UNIQUE INDEX index_currency_name \n" +
                    "ON currency (`name`)");
            database.execSQL("CREATE TABLE `currency_info` ( \n" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "`name` TEXT UNIQUE, \n" +
                    "`full_name` TEXT, \n" +
                    "`full_name_cn` TEXT, \n" +
                    "`flag_location` TEXT \n" +
                    ")");
            database.execSQL("CREATE UNIQUE INDEX index_currency_info_name \n" +
                    "ON currency_info (`name`)");
        }
    };
}
