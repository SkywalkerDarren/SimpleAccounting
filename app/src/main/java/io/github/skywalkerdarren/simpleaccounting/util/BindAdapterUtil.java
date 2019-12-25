package io.github.skywalkerdarren.simpleaccounting.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import java.io.IOException;

/**
 * @author darren
 * @date 2018/4/8
 */

public class BindAdapterUtil extends BaseObservable {
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
        try {
            AssetManager assets = view.getContext().getAssets();
            Bitmap bitmap = BitmapFactory.decodeStream(assets.open(res));
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignore) {

        }
        //Glide.with(view)
        //        .load("file:///android_asset/" + res)
        //        .transition(withCrossFade())
        //        .into(view);
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
