package io.github.skywalkerdarren.simpleaccounting.model.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.util.TypeConvertUtil;

@Database(entities = {Account.class, Bill.class}, version = 1)
@TypeConverters({TypeConvertUtil.class})
public abstract class AccountDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static AccountDatabase INSTANCE;

    public static AccountDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AccountDatabase.class, "accounting.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract AccountDao accountDao();

    public abstract BillDao billDao();

    public abstract TypeDao typeDao();

    //List<Account> accounts = new ArrayList<>(3);
    //    accounts.add(new Account(UUID.randomUUID(), "现金",
    //        "现金金额", BigDecimal.ZERO,
    //        "cash" + PNG, R.color.amber500));
    //    accounts.add(new Account(UUID.randomUUID(), "支付宝",
    //        "在线支付余额", BigDecimal.ZERO,
    //        "alipay" + PNG, R.color.lightblue500));
    //    accounts.add(new Account(UUID.randomUUID(), "微信",
    //        "在线支付余额", BigDecimal.ZERO,
    //        "wechat" + PNG, R.color.lightgreen500));
}
