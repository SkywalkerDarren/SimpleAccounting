package io.github.skywalkerdarren.simpleaccounting.model;

/**
 * Created by darren on 2018/2/12.
 */

public class ExpenseType extends Type {
    /**
     * 支出类型
     */
    private static final ExpenseType EXPENSE_TYPE = new ExpenseType();

    public static ExpenseType getInstance() {
        return EXPENSE_TYPE;
    }

    private static final String[] VALUES = {"其他", "吃喝", "交通", "服饰", "日用品",
            "红包", "话费", "化妆护肤", "娱乐", "医疗"};

    public static String[] getValues() {
        return VALUES;
    }

    @Override
    public boolean getExpense() {
        return true;
    }


}