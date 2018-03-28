package io.github.skywalkerdarren.simpleaccounting.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 收入类型
 *
 * @author darren
 * @date 2018/2/12
 */

public class IncomeType extends BaseType {
    /**
     * 收入类型
     */
    private static final IncomeType INCOME_TYPE = new IncomeType();

    /**
     * @return 获得收入类型
     */
    public static List<BaseType> getTypeList() {
        return INCOME_TYPE.getTypes();
    }

    /**
     * 必要的空构造方法
     */
    private IncomeType() {
        super();
    }

    /**
     * 新建收入类型
     *
     * @param name 名称
     * @param id   资源图片id
     */
    public IncomeType(String name, int id) {
        super(name, id);
    }

    /**
     * 新建收入类型
     *
     * @param name 名称
     * @param id   资源图片id
     */
    public IncomeType(String name, int id, int color) {
        super(name, id, color);
    }

    /*工资#977369
    兼职#a7eef9
    奖金#f4bcb1
    投资#ffecab

    红包#f72e42
    其他#cd533b*/

    @Override
    protected List<BaseType> setDefaultTypes() {
        List<BaseType> types = new ArrayList<>(6);
        types.add(new IncomeType("工资", R.drawable.type_image_wage, Color.rgb(0x97, 0x73, 0x69)));
        types.add(new IncomeType("兼职", R.drawable.type_image_part_time, Color.rgb(0xa7, 0xee, 0xf9)));
        types.add(new IncomeType("奖金", R.drawable.type_image_prize, Color.rgb(0xf4, 0xbc, 0xb1)));
        types.add(new IncomeType("理财投资", R.drawable.type_image_invest, Color.rgb(0xff, 0xec, 0xab)));
        types.add(new IncomeType("红包", R.drawable.type_image_red_package, Color.rgb(0xf7, 0x2e, 0x42)));
        types.add(new IncomeType("其他", R.drawable.type_image_other, Color.rgb(0xcd, 0x53, 0x3b)));
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
