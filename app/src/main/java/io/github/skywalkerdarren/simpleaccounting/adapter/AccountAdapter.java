package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Account;

/**
 * @author darren
 * @date 2018/3/24
 */

public class AccountAdapter extends BaseQuickAdapter<Account, BaseViewHolder> {
    public AccountAdapter(List<Account> data) {
        super(R.layout.item_account, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Account item) {
        helper.setText(R.id.name_text_view, item.getName());
        helper.setText(R.id.hint_text_view, item.getBalanceHint());
        helper.setText(R.id.balance_text_view, item.getBalance().toString());
        helper.setImageResource(R.id.account_type_image_view, item.getImageId());
        ((CardView) helper.getView(R.id.account_type_card_view))
                .setCardBackgroundColor(item.getColor());
    }
}
