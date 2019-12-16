package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.Account;

import static io.github.skywalkerdarren.simpleaccounting.model.Account.FOLDER;

/**
 * @author darren
 * @date 2018/4/13
 */

public class AccountMenuItemViewModel extends BaseObservable {
    private Account mAccount;

    public AccountMenuItemViewModel(Account account) {
        mAccount = account;
    }

    public String getImg() {
        return FOLDER + mAccount.getBitmap();
    }

    public int getColor() {
        return mAccount.getColor();
    }

    public String getName() {
        return mAccount.getName();
    }
}
