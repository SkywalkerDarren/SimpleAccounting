package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 统计列表转换到recycler view的适配器
 *
 * @author darren
 * @date 2018/3/14
 */

public class StatsAdapter extends BaseQuickAdapter<StatsLab.Stats, BaseViewHolder> {

    /**
     * 统计适配器
     *
     * @param data 统计类列表
     */
    public StatsAdapter(@NonNull List<StatsLab.Stats> data) {
        super(R.layout.item_stats, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatsLab.Stats item) {
        helper.setText(R.id.income_text_view, item.getIncome().toString());
        helper.setText(R.id.expense_text_view, item.getExpense().toString());
        helper.setText(R.id.balance_text_view, item.getSum().toString());
        helper.setText(R.id.month_text_view, helper.getAdapterPosition() + 1 + "");
    }
}
