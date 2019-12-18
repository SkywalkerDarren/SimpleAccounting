package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;

import androidx.databinding.BaseObservable;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

import static io.github.skywalkerdarren.simpleaccounting.model.entity.Account.FOLDER;

/**
 * @author darren
 * @date 2018/4/13
 */

public class AccountMenuItemViewModel extends BaseObservable {
    private Account mAccount;
    private Context mContext;

    public AccountMenuItemViewModel(Account account, Context context) {
        mAccount = account;
        mContext = context;
    }

    public String getImg() {
        return FOLDER + mAccount.getBitmap();
    }

    public int getColor() {
        return mContext.getResources().getColor(mAccount.getColorId());
    }

    public String getName() {
        return mAccount.getName();
    }
}
