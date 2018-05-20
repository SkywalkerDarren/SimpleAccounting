package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author darren
 * @date 2018/4/8
 */

public class BindAdapterViewModel extends BaseObservable {
    /**
     * 通过id设置图片
     */
    @BindingAdapter("android:src")
    public static void setTypeImage(ImageView view, Bitmap res) {
        view.setImageBitmap(res);
    }

    @BindingAdapter("android:src")
    public static void setTypeImage(ImageView view, int res) {
        view.setImageResource(res);
    }

    /**
     * 通过色值设置颜色
     */
    @BindingAdapter("android:textColor")
    public static void setBalanceColor(TextView tv, int color) {
        tv.setTextColor(color);
    }

    /**
     * 通过色值设置颜色
     */
    @BindingAdapter("cardBackgroundColor")
    public static void setColor(CardView view, int res) {
        view.setCardBackgroundColor(res);
    }
}
