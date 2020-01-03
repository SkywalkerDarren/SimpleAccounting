package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.Objects;

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

    public ExchangeRateAdapter(Context context) {
        super(R.layout.item_exchange_rate, null);
        mRepository = AppRepository.getInstance(new AppExecutors(), context);
        openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                mRepository.changeCurrencyPosition(Objects.requireNonNull(getItem(from)), Objects.requireNonNull(getItem(to)));

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            }
        });
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
