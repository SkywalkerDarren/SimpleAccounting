package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemClassifyBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

/**
 * 分类页面的统计数据适配器
 *
 * @author darren
 * @date 2018/3/25
 */

public class ClassifyAdapter extends BaseDataBindingAdapter<TypeStats, ItemClassifyBinding> {
    private BigDecimal mSum = BigDecimal.ZERO;
    private final AppRepository mRepository;

    public ClassifyAdapter(Context context) {
        super(R.layout.item_classify, null);
        mRepository = AppRepository.getInstance(new AppExecutors(), context);
    }

    public void setNewList(List<TypeStats> data) {
        setNewDiffData(new TypeStatsDiff(data));
        mSum = BigDecimal.ZERO;
        if (data == null) {
            return;
        }
        for (TypeStats stats : data) {
            mSum = mSum.add(stats.getBalance());
        }
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    protected void convert(ItemClassifyBinding binding, TypeStats item) {
        binding.setStats(item);
        binding.setSum(mSum);
        mRepository.getType(item.getTypeId(), binding::setType);
    }
}
