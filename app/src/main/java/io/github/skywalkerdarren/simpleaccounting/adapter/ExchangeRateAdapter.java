package io.github.skywalkerdarren.simpleaccounting.adapter;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemExchangeRateBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

public class ExchangeRateAdapter extends BaseDraggableDataBindingAdapter<Currency, ItemExchangeRateBinding> {
    private AppRepository mRepository;

    public ExchangeRateAdapter() {
        super(R.layout.item_exchange_rate, null);
        mRepository = AppRepository.getInstance(new AppExecutors(), mContext);
    }

    @Override
    protected void convert(ItemExchangeRateBinding binding, Currency item) {
        binding.setData(item);
        mRepository.getCurrencyInfo(item.getName(), new CurrencyDataSource.LoadCurrencyInfoCallback() {
            @Override
            public void onCurrencyInfoLoaded(CurrencyInfo info) {
                if (info.getFullNameCN() != null) {
                    info.setFullName(info.getFullNameCN());
                }
                if (info.getFullName() == null) {
                    info.setFullName(info.getName());
                }
                binding.setInfo(info);
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }
}
