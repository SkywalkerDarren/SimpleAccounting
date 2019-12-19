package io.github.skywalkerdarren.simpleaccounting.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.StatsDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.util.TypeConvertUtil;

@Database(entities = {Type.class, Account.class, Bill.class}, version = 1)
@TypeConverters(TypeConvertUtil.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract AccountDao accountDao();
    public abstract TypeDao typeDao();
    public abstract BillDao billDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "app.db")
                        //TODO remove this
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract StatsDao statsDao();
}
