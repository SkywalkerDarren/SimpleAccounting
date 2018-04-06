package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.support.annotation.NonNull;

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

public class StatsAdapter extends BaseDataBindingAdapter<StatsLab.Stats, ItemStatsBinding> {

    /**
     * 统计适配器
     *
     * @param data 统计类列表
     */
    public StatsAdapter(@NonNull List<StatsLab.Stats> data) {
        super(R.layout.item_stats, data);
    }

    @Override
    protected void convert(ItemStatsBinding binding, StatsLab.Stats item) {
        binding.setStats(new StatsItemViewModel(item, getParentPosition(item) + 1));
    }
}
