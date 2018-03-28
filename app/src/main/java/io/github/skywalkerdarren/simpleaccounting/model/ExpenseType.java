package io.github.skywalkerdarren.simpleaccounting.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 支出类型
 *
 * @author darren
 * @date 2018/2/12
 */

public class ExpenseType extends BaseType {
    /**
     * 支出类型
     */
    private static final ExpenseType EXPENSE_TYPE = new ExpenseType();

    /**
     * @return 支出类型列表
     */
    public static List<BaseType> getTypeList() {
        return EXPENSE_TYPE.getTypes();
    }

    /**
     * 必要的空构造方法
     */
    private ExpenseType() {
        super();
    }

    /**
     * 新建支出类型
     *
     * @param name 名称
     * @param id   资源图片id
     */
    public ExpenseType(String name, int id) {
        super(name, id);
    }

    /**
     * 新建支出类型
     *
     * @param name  名称
     * @param id    资源图片id
     * @param color 颜色
     */
    public ExpenseType(String name, int id, int color) {
        super(name, id, color);
    }


    @Override
    protected List<BaseType> setDefaultTypes() {
        List<BaseType> types = new ArrayList<>(10);
        /*吃喝#e6c453
        服饰#7eb79f
        话费#836cab
        娱乐#73c8d5
        日用品#f08d78
        化妆#fe4c5e
        交通#f9d55d
        医疗#c0f1f9*/

        /*红包#f72e42
        其他#cd533b*/
        types.add(new ExpenseType("吃喝", R.drawable.type_image_diet, Color.rgb(0xe6, 0xc4, 0x53)));
        types.add(new ExpenseType("娱乐", R.drawable.type_image_entertainment, Color.rgb(0x73, 0xc8, 0xd5)));
        types.add(new ExpenseType("交通", R.drawable.type_image_traffic, Color.rgb(0xf9, 0xd5, 0x5d)));
        types.add(new ExpenseType("日用品", R.drawable.type_image_daily_necessities, Color.rgb(0xf0, 0x8d, 0x78)));
        types.add(new ExpenseType("化妆护肤", R.drawable.type_image_make_up, Color.rgb(0xfe, 0x4c, 0x5e)));
        types.add(new ExpenseType("医疗", R.drawable.type_image_medical, Color.rgb(0xc0, 0xf1, 0xf9)));
        types.add(new ExpenseType("服饰", R.drawable.type_image_apparel, Color.rgb(0x7e, 0xb7, 0x9f)));
        types.add(new ExpenseType("话费", R.drawable.type_image_calls, Color.rgb(0x83, 0x6c, 0xab)));
        types.add(new ExpenseType("红包", R.drawable.type_image_red_package, Color.rgb(0xf7, 0x2e, 0x42)));
        types.add(new ExpenseType("其他", R.drawable.type_image_other, Color.rgb(0xcd, 0x53, 0x3b)));
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