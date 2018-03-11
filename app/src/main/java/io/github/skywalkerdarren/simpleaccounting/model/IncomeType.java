package io.github.skywalkerdarren.simpleaccounting.model;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * Created by darren on 2018/2/12.
 */

public class IncomeType extends BaseType {
    /**
     * 收入类型
     */
    private static final IncomeType INCOME_TYPE = new IncomeType();

    public static List<BaseType> getTypeList() {
        return INCOME_TYPE.getTypes();
    }

    private IncomeType() {
        super();
    }

    public IncomeType(String name, int id) {
        super(name, id);
    }

    @Override
    protected List<BaseType> setDefaultTypes() {
        List<BaseType> types = new ArrayList<>(6);
        types.add(new IncomeType("工资", R.drawable.type_image_wage));
        types.add(new IncomeType("兼职", R.drawable.type_image_part_time));
        types.add(new IncomeType("奖金", R.drawable.type_image_prize));
        types.add(new IncomeType("理财投资", R.drawable.type_image_invest));
        types.add(new IncomeType("红包", R.drawable.type_image_red_package));
        types.add(new IncomeType("其他", R.drawable.type_image_other));
        return types;
    }

    @Deprecated
    public static final String[] VALUES = {"兼职", "奖金", "工资", "理财投资", "红包", "其他",};

    @Deprecated
    public static String[] getValues() {
        return VALUES;
    }

    @Override
    public boolean getExpense() {
        return false;
    }
}
