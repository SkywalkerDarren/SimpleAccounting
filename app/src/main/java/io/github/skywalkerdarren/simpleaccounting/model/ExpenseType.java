package io.github.skywalkerdarren.simpleaccounting.model;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * Created by darren on 2018/2/12.
 */

public class ExpenseType extends BaseType {
    /**
     * 支出类型
     */
    private static final ExpenseType EXPENSE_TYPE = new ExpenseType();

    public static List<BaseType> getTypeList() {
        return EXPENSE_TYPE.getTypes();
    }

    private ExpenseType() {
        super();
    }

    public ExpenseType(String name, int id) {
        super(name, id);
    }

    @Override
    protected List<BaseType> setDefaultTypes() {
        List<BaseType> types = new ArrayList<>(10);
        types.add(new ExpenseType("吃喝", R.drawable.type_image_diet));
        types.add(new ExpenseType("娱乐", R.drawable.type_image_entertainment));
        types.add(new ExpenseType("交通", R.drawable.type_image_traffic));
        types.add(new ExpenseType("日用品", R.drawable.type_image_daily_necessities));
        types.add(new ExpenseType("化妆护肤", R.drawable.type_image_make_up));
        types.add(new ExpenseType("医疗", R.drawable.type_image_medical));
        types.add(new ExpenseType("服饰", R.drawable.type_image_apparel));
        types.add(new ExpenseType("话费", R.drawable.type_image_calls));
        types.add(new ExpenseType("红包", R.drawable.type_image_red_package));
        types.add(new ExpenseType("其他", R.drawable.type_image_other));
        return types;
    }

    @Deprecated
    private static final String[] VALUES = {"其他", "吃喝", "交通", "服饰", "日用品",
            "红包", "话费", "化妆护肤", "娱乐", "医疗"};

    @Deprecated
    public static String[] getValues() {
        return VALUES;
    }


    @Override
    public boolean getExpense() {
        return true;
    }

}