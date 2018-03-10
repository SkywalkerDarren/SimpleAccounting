package io.github.skywalkerdarren.simpleaccounting.model;

import java.math.BigDecimal;

/**
 * Created by darren on 2018/3/10.
 * 大数计算工具
 */

public class CalculateUtil {
    //TODO 计算器

    /**
     * 获取计算结果
     *
     * @return
     */
    public static BigDecimal getResult(Expression expression) {
        CharSequence exp = ConvertToExpression(expression);
        return BigDecimal.TEN;
    }

    public static CharSequence ConvertToExpression(Expression exp) {
        return exp.createExpression();
    }
}
