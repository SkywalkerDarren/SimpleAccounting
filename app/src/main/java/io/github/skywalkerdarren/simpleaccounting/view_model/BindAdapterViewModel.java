package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import io.github.skywalkerdarren.simpleaccounting.model.Type;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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

    @BindingAdapter("android:src")
    public static void setTypeImage(ImageView view, String res) {
        Glide.with(view)
                .load("file:///android_asset/" + res)
                .transition(withCrossFade())
                .into(view);
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
