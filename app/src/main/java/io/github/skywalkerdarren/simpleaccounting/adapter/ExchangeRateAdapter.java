package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;
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
    public void setNewData(@Nullable List<Currency> data) {
        if (checkCache(data)) {
            super.setNewData(data);
        }
    }

    private boolean checkCache(List<Currency> currencies) {
        if (currencies == null || mData.size() != currencies.size()) {
            return true;
        }
        for (int i = 0; i < currencies.size(); i++) {
            if (!currencies.get(i).equals(mData.get(i))) {
                return true;
            }
        }
        return false;
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

    @Override
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }
}
