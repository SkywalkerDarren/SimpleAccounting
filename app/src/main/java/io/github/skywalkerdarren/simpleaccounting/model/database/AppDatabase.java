package io.github.skywalkerdarren.simpleaccounting.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyInfoDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.CurrencyRateDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.TypeConvertUtil;

import static io.github.skywalkerdarren.simpleaccounting.model.database.MigrationDb.MIGRATION_1_2;

@Database(entities = {Type.class, Account.class, Bill.class, Currency.class, CurrencyInfo.class}, version = 2)
@TypeConverters(TypeConvertUtil.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "app.db")
                        .fallbackToDestructiveMigration()
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract AccountDao accountDao();

    public abstract TypeDao typeDao();

    public abstract BillDao billDao();

    public abstract StatsDao statsDao();

    public abstract CurrencyRateDao currencyRateDao();

    public abstract CurrencyInfoDao currencyInfoDao();
}
