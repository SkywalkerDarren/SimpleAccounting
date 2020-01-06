package io.github.skywalkerdarren.simpleaccounting.util.calculate;

/**
 * 处理表达式
 *
 * @author darren
 * @date 2018/3/10
 */

public interface Expression {
    /**
     * 处理并返回只包含数字，加减乘除，小数点的表达式
     * eg. "3+3*3/3-3"
     *
     * @return 合法表达式
     */
    String createExpression();
}
