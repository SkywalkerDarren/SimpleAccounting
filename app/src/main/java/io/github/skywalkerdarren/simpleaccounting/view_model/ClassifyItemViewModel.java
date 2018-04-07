package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 分类项目vm
 *
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

    /**
     * 设置图片
     */
    @BindingAdapter("src")
    public static void setImg(ImageView view, int res) {
        view.setImageResource(res);
    }

    /**
     * @return 类型名称
     */
    public String getName() {
        return mStats.getType().getName();
    }

    /**
     * @return 类型图片id
     */
    public int getImg() {
        return mStats.getType().getTypeId();
    }

    /**
     * @return 总盈余
     */
    public String getBalance() {
        return mStats.getSum().toString();
    }

    /**
     * @return 当前百分比
     */
    public String getPresent() {
        BigDecimal decimal = mStats.getSum()
                .multiply(BigDecimal.valueOf(100))
                .divide(mSum, 2, BigDecimal.ROUND_HALF_UP);
        return decimal.toPlainString() + "%";
    }
}
