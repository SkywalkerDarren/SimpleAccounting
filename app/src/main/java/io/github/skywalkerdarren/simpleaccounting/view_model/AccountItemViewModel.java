package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.Account;

/**
 * 账户vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class AccountItemViewModel extends BaseObservable {
    private Account mAccount;

    public AccountItemViewModel(Account account) {
        mAccount = account;
    }

    /**
     * @return 账户图片id
     */
    public int getImg() {
        return mAccount.getImageId();
    }

    /**
     * @return 账户背景色值
     */
    public int getColor() {
        return mAccount.getColor();
    }

    /**
     * @return 账户盈余
     */
    public String getBalance() {
        return mAccount.getBalance().toString();
    }

    /**
     * @return 账户名
     */
    public String getName() {
        return mAccount.getName();
    }

    /**
     * @return 账户备注
     */
    public String getBalanceHint() {
        return mAccount.getBalanceHint();
    }
}
