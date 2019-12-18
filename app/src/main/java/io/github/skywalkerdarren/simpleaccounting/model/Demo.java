package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

/**
 * @author darren
 * @date 2018/5/22
 */

public class Demo {
    private final Random mRandom;
    private List<Type> mExpense;
    private List<Type> mIncome;
    private List<Account> mAccounts;
    private BillLab mLab;
    private static final String TAG = "Demo";

    public Demo(Context context) {
        mExpense = TypeLab.getInstance(context).getTypes(true);
        mIncome = TypeLab.getInstance(context).getTypes(false);
        mAccounts = AccountLab.getInstance(context).getAccounts();
        mLab = BillLab.getInstance(context);
        mRandom = new Random(DateTime.now().getMillis());
    }

    private int getRandomInt(int bound) {
        return mRandom.nextInt(bound);
    }

    public void createRandomBill(DateTime start, DateTime end) {
        Bill bill = new Bill();

        // 1/10 income
        Type type;
        BigDecimal balance;
        if (getRandomInt(10) > 1) {
            type = mExpense.get(getRandomInt(mExpense.size()));
            type.setIsExpense(true);
            // 1-201
            balance = new BigDecimal(getRandomFloat(200, 1));
        } else {
            type = mIncome.get(getRandomInt(mIncome.size()));
            type.setIsExpense(false);
            // 500-1.5k
            balance = new BigDecimal(getRandomFloat(1000, 500));
        }
        bill.setTypeId(type.getUUID());
        bill.setName(type.getName());
        bill.setBalance(balance);

        bill.setAccountId(mAccounts.get(getRandomInt(mAccounts.size())).getUUID());

        long p = end.getMillis() - start.getMillis();
        p = mRandom.nextLong() % p;
        bill.setDate(start.plus(p));

        if (!(getRandomInt(3) > 1)) {
            bill.setRemark("这是备注");
        }

        Log.d(TAG, "createRandomBill: " + p);
        mLab.addBill(bill);
    }

    private float getRandomFloat(int range, int base) {
        return mRandom.nextFloat() * range + base;
    }

    public void createRandomBill(int cnt, DateTime start, DateTime end) {
        for (int i = 0; i < cnt; i++) {
            createRandomBill(start, end);
        }
    }
}
