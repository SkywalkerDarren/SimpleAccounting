package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.content.Context;

import androidx.databinding.BaseObservable;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 分类项目vm
 *
 * @author darren
 * @date 2018/4/6
 */

public class ClassifyItemViewModel extends BaseObservable {
    private TypeStats mStats;
    private Type mType;
    private BigDecimal mSum;

    public ClassifyItemViewModel(TypeStats stats, BigDecimal sum, Context context) {
        mStats = stats;
        mSum = sum;
        mType = AppRepositry.getInstance(context).getType(stats.getTypeId());
    }

    /**
     * @return 类型名称
     */
    public String getName() {
        return mType.getName();
    }

    /**
     * @return 类型图片id
     */
    public String getImg() {
        return Type.FOLDER + mType.getAssetsName();
    }

    /**
     * @return 总盈余
     */
    public String getBalance() {
        return FormatUtil.getNumeric(mStats.getBalance());
    }

    /**
     * @return 当前百分比
     */
    public String getPresent() {
        BigDecimal decimal = mStats.getBalance()
                .multiply(BigDecimal.valueOf(100))
                .divide(mSum, 2, BigDecimal.ROUND_HALF_UP);
        return decimal.toPlainString() + "%";
    }
}
