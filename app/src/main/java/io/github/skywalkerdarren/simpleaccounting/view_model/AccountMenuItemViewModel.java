package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.graphics.Bitmap;

import io.github.skywalkerdarren.simpleaccounting.model.Account;

/**
 * @author darren
 * @date 2018/4/13
 */

public class AccountMenuItemViewModel extends BaseObservable {
    private Account mAccount;

    public AccountMenuItemViewModel(Account account){
        mAccount = account;
    }

    public Bitmap getImg() {
        return mAccount.getBitmap();
    }

    public int getColor() {
        return mAccount.getColor();
    }

    public String getName() {
        return mAccount.getName();
    }
}
