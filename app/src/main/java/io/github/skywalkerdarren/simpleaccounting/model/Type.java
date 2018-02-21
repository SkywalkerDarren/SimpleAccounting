package io.github.skywalkerdarren.simpleaccounting.model;

import android.support.annotation.IdRes;

import java.util.HashMap;
import java.util.Map;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * Created by darren on 2018/2/12.
 */

public abstract class Type {
    private static final Map<String, Integer> TYPE = new HashMap<>();

    public static Map<String, Integer> getType() {
        if (TYPE.isEmpty()) {
            TYPE.put("兼职", R.drawable.type_image_part_time);
            TYPE.put("奖金", R.drawable.type_image_prize);
            TYPE.put("工资", R.drawable.type_image_wage);
            TYPE.put("理财投资", R.drawable.type_image_invest);
            TYPE.put("红包", R.drawable.type_image_red_package);
            TYPE.put("其他", R.drawable.type_image_other);
            TYPE.put("交通", R.drawable.type_image_traffic);
            TYPE.put("化妆护肤", R.drawable.type_image_make_up);
            TYPE.put("医疗", R.drawable.type_image_medical);
            TYPE.put("吃喝", R.drawable.type_image_diet);
            TYPE.put("娱乐", R.drawable.type_image_entertainment);
            TYPE.put("日用品", R.drawable.type_image_daily_necessities);
            TYPE.put("服饰", R.drawable.type_image_apparel);
            TYPE.put("话费", R.drawable.type_image_calls);
        }
        return TYPE;
    }

    public abstract boolean getExpense();

    public void addType(String name, @IdRes int id) {
        TYPE.put(name, id);
    }
}
