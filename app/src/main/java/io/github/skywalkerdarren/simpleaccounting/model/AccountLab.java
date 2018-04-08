package io.github.skywalkerdarren.simpleaccounting.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.AccountTable.Cols;

/**
 * @author darren
 * @date 2018/3/25
 */

public class AccountLab {

    private static AccountLab sAccountLab;
    private final SQLiteDatabase mDatabase;
    private Context mContext;

    public AccountLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public static AccountLab getInstance(Context context) {
        if (sAccountLab == null) {
            return sAccountLab = new AccountLab(context);
        } else {
            return sAccountLab;
        }
    }

    private static ContentValues getContentValues(Account account) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, account.getId().toString());
        values.put(Cols.NAME, account.getName());
        values.put(Cols.BALANCE, account.getBalance().toString());
        values.put(Cols.BALANCE_HINT, account.getBalanceHint());
        values.put(Cols.IMAGE_ID, account.getImageId());
        values.put(Cols.COLOR_ID, account.getColorId());
        return values;
    }

    private AccountCursorWrapper queryAccounts(String where, String[] args) {
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(DbSchema.AccountTable.TABLE_NAME,
                null,
                where,
                args,
                null,
                null,
                null);
        return new AccountCursorWrapper(cursor);
    }

    public Account getAccount(UUID uuid) {
        try (AccountCursorWrapper cursor = queryAccounts(Cols.UUID + " == ?",
                new String[]{uuid.toString()})) {
            cursor.moveToFirst();
            // 转换colorId到color
            Account account = cursor.getAccount();
            account.setColor(mContext.getResources().getColor(account.getColorId()));
            return account;
        }
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>(3);
        try (AccountCursorWrapper cursor = queryAccounts(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // 转换colorId到color
                Account account = cursor.getAccount();
                account.setColor(mContext.getResources().getColor(account.getColorId()));
                accounts.add(account);
                cursor.moveToNext();
            }
        }
        return accounts;
    }

    static void initAccountDb(SQLiteDatabase sqLiteDatabase) {
        List<Account> accounts = new ArrayList<>(3);
        accounts.add(new Account().setName("现金").setBalanceHint("现金金额")
                .setImageId(R.drawable.account_cash)
                .setColorId(R.color.amber500)
                .setBalance(BigDecimal.ZERO));
        accounts.add(new Account().setName("支付宝").setBalanceHint("在线支付余额")
                .setImageId(R.drawable.account_alipay)
                .setColorId(R.color.lightblue500)
                .setBalance(BigDecimal.ZERO));
        accounts.add(new Account().setName("微信").setBalanceHint("在线支付余额")
                .setImageId(R.drawable.account_wechat)
                .setColorId(R.color.lightgreen500)
                .setBalance(BigDecimal.ZERO));
        for (Account account : accounts) {
            sqLiteDatabase.insert(DbSchema.AccountTable.TABLE_NAME, null,
                    getContentValues(account));
        }
    }

    public void updateAccount(Account account) {
        ContentValues values = getContentValues(account);
        mDatabase.update(DbSchema.AccountTable.TABLE_NAME, values, Cols.UUID + " = ?",
                new String[]{account.getId().toString()});
    }

    public void delAccount(UUID uuid) {
        mDatabase.delete(DbSchema.AccountTable.TABLE_NAME, Cols.UUID + " = ?",
                new String[]{uuid.toString()});
    }
}
