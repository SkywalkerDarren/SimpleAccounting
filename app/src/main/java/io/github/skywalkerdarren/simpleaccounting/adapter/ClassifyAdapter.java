package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 分类页面的统计数据适配器
 *
 * @author darren
 * @date 2018/3/25
 */

public class ClassifyAdapter extends BaseQuickAdapter<StatsLab.TypeStats, BaseViewHolder> {
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
    protected void convert(BaseViewHolder helper, StatsLab.TypeStats item) {
        helper.setText(R.id.type_text_view, item.getType().getName());
        helper.setImageResource(R.id.type_image_view, item.getType().getTypeId());
        helper.setText(R.id.balance_text_view, item.getSum().toString());
        BigDecimal decimal = item.getSum()
                .multiply(BigDecimal.valueOf(100))
                .divide(mSum, 2, BigDecimal.ROUND_HALF_UP);
        helper.setText(R.id.present_text_view, decimal.toPlainString() + "%");
    }
}
