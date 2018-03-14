package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * Created by darren on 2018/3/10.
 */

public interface Expression {
    /**
     * 获得表达式
     *
     * @return 只包含数字，加减乘除，小数点的表达式
     */
    String createExpression();
}
