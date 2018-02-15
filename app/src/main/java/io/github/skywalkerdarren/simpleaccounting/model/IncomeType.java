package io.github.skywalkerdarren.simpleaccounting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by darren on 2018/2/12.
 */

public class IncomeType implements Type {
    /**
     * 收入类型
     */

    private final static IncomeType sIncomeType = new IncomeType();

    public static IncomeType getInstance() {
        return sIncomeType;
    }

    private static final List<String> TYPE = new ArrayList<>();

    private static final String[] VALUES = {"其他", "工资", "奖金", "红包", "转账", "投资"};

    @Override
    public List<String> getType() {
        if (TYPE.size() < VALUES.length) {
            Collections.addAll(TYPE, VALUES);
        }
        return TYPE;
    }

    @Override
    public boolean getExpense() {
        return false;
    }
}
