package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.app.Application;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * @author darren
 * @date 2018/3/24
 */

public class AccountAdapter extends BaseDraggableDataBindingAdapter<Account, ItemAccountBinding> {
    private final AppRepository mRepository;

    public AccountAdapter(List<Account> data, Application application) {
        super(R.layout.item_account, data);
        mRepository = AppRepository.getInstance(new AppExecutors(), application);
    }

    @Override
    protected void convert(ItemAccountBinding binding, Account item) {
        binding.setAccount(item);
        mRepository.getAccountStats(item.getUUID(), new DateTime(0), DateTime.now(), accountStats ->
                binding.balanceTextView.setText(FormatUtil.getNumeric(accountStats.getSum())));
    }
}
