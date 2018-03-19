package io.github.skywalkerdarren.simpleaccounting.adapter;

/**
 * 账单分隔符接口
 *
 * @author darren
 * @date 2018/2/16
 */

public interface HeaderDivider {
    /**
     * 获取账单支出类型名称
     *
     * @return 名称
     */
    String getExpense();

    /**
     * 获取账单收入类型名称
     *
     * @return 名称
     */
    String getIncome();
}
