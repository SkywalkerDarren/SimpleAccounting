package io.github.skywalkerdarren.simpleaccounting.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemStatsBinding;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.StatsItemViewModel;

/**
 * 统计列表转换到recycler view的适配器
 *
 * @author darren
 * @date 2018/3/14
 */

public class StatsAdapter extends BaseDataBindingAdapter<StatsLab.BillStats, ItemStatsBinding> {
    /**
     * 统计适配器
     *
     * @param data 统计类列表
     */
    public StatsAdapter(@NonNull List<StatsLab.BillStats> data) {
        super(R.layout.item_stats, data);
    }

    @Override
    public void setNewData(@Nullable List<StatsLab.BillStats> data) {
        super.setNewData(data);
    }

    @Override
    protected void convert(ItemStatsBinding binding, StatsLab.BillStats item) {
        binding.setStats(new StatsItemViewModel(item, mData.indexOf(item) + 1));
    }
}
