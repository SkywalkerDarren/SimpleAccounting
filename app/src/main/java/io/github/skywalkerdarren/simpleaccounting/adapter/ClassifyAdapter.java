package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemClassifyBinding;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyItemViewModel;

/**
 * 分类页面的统计数据适配器
 *
 * @author darren
 * @date 2018/3/25
 */

public class ClassifyAdapter extends BaseDataBindingAdapter<StatsLab.TypeStats, ItemClassifyBinding> {
    private BigDecimal mSum = BigDecimal.ZERO;

    public ClassifyAdapter(@Nullable List<StatsLab.TypeStats> data) {
        super(R.layout.item_classify, data);
        if (data == null) {
            return;
        }
        for (StatsLab.TypeStats stats : data) {
            mSum = mSum.add(stats.getSum());
        }
    }

    @Override
    public void setNewData(List<StatsLab.TypeStats> data) {
        super.setNewData(data);
        mSum = BigDecimal.ZERO;
        if (data == null) {
            return;
        }
        for (StatsLab.TypeStats stats : data) {
            mSum = mSum.add(stats.getSum());
        }
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    protected void convert(ItemClassifyBinding binding, StatsLab.TypeStats item) {
        binding.setClassify(new ClassifyItemViewModel(item, mSum));
    }
}
