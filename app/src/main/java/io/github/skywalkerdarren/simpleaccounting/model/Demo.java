package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import kotlin.Unit;

/**
 * @author darren
 * @date 2018/5/22
 */

public class Demo {
    private static final String TAG = "Demo";
    private final Random mRandom;
    private final CountDownLatch mLatch = new CountDownLatch(3);
    private List<Type> mExpense;
    private List<Type> mIncome;
    private List<Account> mAccounts;
    private AppRepository mRepository;

    public Demo(Context context) {
        mRepository = AppRepository.getInstance(new AppExecutors(), context);
        mRepository.getTypesOnBackground(true, types -> {
            mExpense = types;
            mLatch.countDown();
            return Unit.INSTANCE;
        });
        mRepository.getTypesOnBackground(false, types -> {
            mIncome = types;
            mLatch.countDown();
            return Unit.INSTANCE;
        });
        mRepository.getAccountsOnBackground(accounts -> {
            mAccounts = accounts;
            mLatch.countDown();
            return Unit.INSTANCE;
        });
        try {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mRandom = new Random(DateTime.now().getMillis());
    }

    private int getRandomInt(int bound) {
        return mRandom.nextInt(bound);
    }

    private void createRandomBill(DateTime start, DateTime end) {

        // 1/10 income
        Type type;
        BigDecimal balance;
        if (getRandomInt(10) > 1) {
            type = mExpense.get(getRandomInt(mExpense.size()));
            type.setExpense(true);
            // 1-201
            balance = new BigDecimal(getRandomFloat(200, 1));
        } else {
            type = mIncome.get(getRandomInt(mIncome.size()));
            type.setExpense(false);
            // 500-1.5k
            balance = new BigDecimal(getRandomFloat(1000, 500));
        }
        long p = end.getMillis() - start.getMillis();
        p = mRandom.nextLong() % p;

        Bill bill = new Bill(
                type.getUuid(),
                Objects.requireNonNull(mAccounts.get(getRandomInt(mAccounts.size())).getUuid()),
                start.plus(p),
                type.getName(),
                balance);

        if (!(getRandomInt(3) > 1)) {
            bill.setRemark("这是备注");
        }

        Log.d(TAG, "createRandomBill: " + p);
        mRepository.addBill(bill);
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
