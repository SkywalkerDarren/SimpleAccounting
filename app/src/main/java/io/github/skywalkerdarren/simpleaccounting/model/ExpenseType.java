package io.github.skywalkerdarren.simpleaccounting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by darren on 2018/2/12.
 */

public class ExpenseType implements Type {
    /**
     * 支出类型
     */
    private static final ExpenseType EXPENSE_TYPE = new ExpenseType();

    public static ExpenseType getInstance() {
        return EXPENSE_TYPE;
    }

    private static final List<String> TYPE = new ArrayList<>();
    private static final String[] VALUES = {"其他", "吃喝", "交通", "服饰", "化妆",
            "日用品", "红包", "话费", "电子产品", "娱乐",
            "医疗", "宠物", "养车", "买菜", "孩子", "教育",
            "居家", "转账"};

    @Override
    public List<String> getType() {
        if (TYPE.size() < VALUES.length) {
            Collections.addAll(TYPE, VALUES);
        }
        return TYPE;
    }

    @Override
    public boolean getExpense() {
        return true;
    }

}