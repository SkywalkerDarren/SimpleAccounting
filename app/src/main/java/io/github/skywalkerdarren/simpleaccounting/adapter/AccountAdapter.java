package io.github.skywalkerdarren.simpleaccounting.adapter;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountItemViewModel;

/**
 * @author darren
 * @date 2018/3/24
 */

public class AccountAdapter extends BaseDraggableDataBindingAdapter<Account, ItemAccountBinding> {
    public AccountAdapter(List<Account> data) {
        super(R.layout.item_account, data);
    }
    @Override
    protected void convert(ItemAccountBinding binding, Account item) {
        binding.setAccount(new AccountItemViewModel(item));
    }
}
