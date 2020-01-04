package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.content.Context;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemCurrencyBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import kotlin.Pair;

/**
 * @author darren
 * @date 2018/4/13
 */

public class AllCurrenciesAdapter extends BaseDataBindingAdapter<Pair<Currency, CurrencyInfo>, ItemCurrencyBinding> {
    private final AppRepository mRepository;

    public AllCurrenciesAdapter(Context context) {
        super(R.layout.item_currency, null);
        mRepository = AppRepository.getInstance(new AppExecutors(), context);
    }

    @Override
    protected void convert(ItemCurrencyBinding binding, Pair<Currency, CurrencyInfo> item) {
        binding.setCurrency(item.getFirst());
        binding.setInfo(item.getSecond());
        binding.favCurrency.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (item.getFirst() != null) {
                mRepository.setCurrencyFav(item.getFirst().getName(), isChecked);
            }
        });
    }
}
