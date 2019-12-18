package io.github.skywalkerdarren.simpleaccounting.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.github.skywalkerdarren.simpleaccounting.model.dao.AccountDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.dao.TypeDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

@Database(entities = {Type.class, Account.class, Bill.class}, version = 1)
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
                        .build();
            }
            return INSTANCE;
        }
    }

    //accountDao().newAccount(new Account("现金","现金金额",
    //                        BigDecimal.ZERO,"cash.png", R.color.amber500));
    //accountDao().newAccount(new Account("支付宝","在线支付余额",
    //                        BigDecimal.ZERO,"alipay.png", R.color.lightblue500));
    //accountDao().newAccount(new Account("微信","在线支付余额",
    //                        BigDecimal.ZERO,"wechat.png", R.color.lightgreen500));
    //
    //typeDao().newType(new Type("吃喝", Color.rgb(0xe6, 0xc4, 0x53),
    //            true,"diet.png"));
    //typeDao().newType(new Type("娱乐", Color.rgb(0x73, 0xc8, 0xd5),
    //            true,"entertainment.png"));
    //typeDao().newType(new Type("交通", Color.rgb(0xf9, 0xd5, 0x5d),
    //            true,"traffic.png"));
    //typeDao().newType(new Type("日用品", Color.rgb(0xf0, 0x8d, 0x78),
    //            true,"daily_necessities.png"));
    //typeDao().newType(new Type("化妆护肤", Color.rgb(0xfe, 0x4c, 0x5e),
    //            true,"make_up.png"));
    //typeDao().newType(new Type("医疗", Color.rgb(0xc0, 0xf1, 0xf9),
    //            true,"medical.png"));
    //typeDao().newType(new Type("服饰", Color.rgb(0x7e, 0xb7, 0x9f),
    //            true,"apparel.png"));
    //typeDao().newType(new Type("话费", Color.rgb(0x83, 0x6c, 0xab),
    //            true,"calls.png"));
    //typeDao().newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
    //            true,"red_package.png"));
    //typeDao().newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
    //            true,"other.png"));
    //
    //typeDao().newType(new Type("工资", Color.rgb(0x97, 0x73, 0x69),
    //            false,"wage.png"));
    //typeDao().newType(new Type("兼职", Color.rgb(0xa7, 0xee, 0xf9),
    //            false,"part_time.png"));
    //typeDao().newType(new Type("奖金", Color.rgb(0xf4, 0xbc, 0xb1),
    //            false,"prize.png"));
    //typeDao().newType(new Type("理财投资", Color.rgb(0xff, 0xec, 0xab),
    //            false,"invest.png"));
    //typeDao().newType(new Type("红包", Color.rgb(0xf7, 0x2e, 0x42),
    //            false,"red_package.png"));
    //typeDao().newType(new Type("其他", Color.rgb(0xcd, 0x53, 0x3b),
    //            false,"other.png"));
}
