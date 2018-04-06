package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * @author darren
 * @date 2018/4/6
 */

public class ClassifyItemViewModel extends BaseObservable {
    private StatsLab.TypeStats mStats;
    private BigDecimal mSum;

    public ClassifyItemViewModel(StatsLab.TypeStats stats, BigDecimal sum) {
        mStats = stats;
        mSum = sum;
    }

    @BindingAdapter("src")
    public static void setImg(ImageView view, int res) {
        view.setImageResource(res);
    }

    public String getName() {
        return mStats.getType().getName();
    }

    public int getImg() {
        return mStats.getType().getTypeId();
    }

    public String getBalance() {
        return mStats.getSum().toString();
    }

    public String getPresent() {
        BigDecimal decimal = mStats.getSum()
                .multiply(BigDecimal.valueOf(100))
                .divide(mSum, 2, BigDecimal.ROUND_HALF_UP);
        return decimal.toPlainString() + "%";
    }
}
