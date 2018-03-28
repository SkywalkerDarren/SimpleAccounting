package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * 分类页面的统计数据适配器
 *
 * @author darren
 * @date 2018/3/25
 */

public class ClassifyAdapter extends BaseQuickAdapter<BillLab.TypeStats, BaseViewHolder> {
    private BigDecimal mSum;

    public ClassifyAdapter(@Nullable List<BillLab.TypeStats> data) {
        super(R.layout.classify_item, data);
        if (data == null) {
            return;
        }
        mSum = BigDecimal.ZERO;
        for (BillLab.TypeStats stats : data) {
            mSum = mSum.add(stats.getSum());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, BillLab.TypeStats item) {
        helper.setText(R.id.type_text_view, item.getType().getName());
        helper.setImageResource(R.id.type_image_view, item.getType().getTypeId());
        helper.setText(R.id.balance_text_view, item.getSum().toString());
        BigDecimal decimal = item.getSum()
                .multiply(BigDecimal.valueOf(100))
                .divide(mSum, 2, BigDecimal.ROUND_HALF_UP);
        helper.setText(R.id.present_text_view, decimal.toPlainString() + "%");
    }
}
