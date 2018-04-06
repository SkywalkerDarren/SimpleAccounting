package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import io.github.skywalkerdarren.simpleaccounting.model.Account;

/**
 * @author darren
 * @date 2018/4/6
 */

public class AccountItemViewModel extends BaseObservable {
    private Account mAccount;

    public AccountItemViewModel(Account account) {
        mAccount = account;
    }

    @BindingAdapter("src")
    public static void setImg(ImageView view, int res) {
        view.setImageResource(res);
    }

    @BindingAdapter("cardBackgroundColor")
    public static void setColor(CardView view, int color) {
        view.setCardBackgroundColor(color);
    }

    public int getImg() {
        return mAccount.getImageId();
    }

    public int getColor() {
        return mAccount.getColor();
    }

    public String getBalance() {
        return mAccount.getBalance().toString();
    }

    public String getName() {
        return mAccount.getName();
    }

    public String getBalanceHint() {
        return mAccount.getBalanceHint();
    }
}
