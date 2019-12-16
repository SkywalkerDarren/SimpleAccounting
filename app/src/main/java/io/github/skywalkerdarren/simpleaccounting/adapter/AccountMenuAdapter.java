package io.github.skywalkerdarren.simpleaccounting.adapter;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.MenuAccountItemBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountMenuItemViewModel;

/**
 * @author darren
 * @date 2018/4/13
 */

public class AccountMenuAdapter extends BaseDataBindingAdapter<Account, MenuAccountItemBinding> {
    public AccountMenuAdapter(List<Account> accounts) {
        super(R.layout.menu_account_item, accounts);
    }

    @Override
    protected void convert(MenuAccountItemBinding binding, Account item) {
        binding.setAccount(new AccountMenuItemViewModel(item));
    }
}
