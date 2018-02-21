package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * Created by darren on 2018/2/12.
 */

public class IncomeType extends Type {
    /**
     * 收入类型
     */

    private final static IncomeType sIncomeType = new IncomeType();

    public static IncomeType getInstance() {
        return sIncomeType;
    }

    public static final String[] VALUES = {"兼职", "奖金", "工资", "理财投资", "红包", "其他",};


    public static String[] getValues() {
        return VALUES;
    }

    @Override
    public boolean getExpense() {
        return false;
    }

}
