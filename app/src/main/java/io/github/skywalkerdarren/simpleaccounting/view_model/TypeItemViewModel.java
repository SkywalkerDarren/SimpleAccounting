package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Type;

/**
 * 类型项目vm
 *
 * @author darren
 * @date 2018/4/7
 */

public class TypeItemViewModel extends BaseObservable {
    private Type mType;
    private FragmentBillEditBinding mBinding;
    private Context mContext;

    public TypeItemViewModel(Type type, FragmentBillEditBinding binding, Context context) {
        mType = type;
        mBinding = binding;
        mContext = context;
    }

    /**
     * 通过id设置图片
     */
    @BindingAdapter("src")
    public static void setImg(ImageView view, int res) {
        view.setImageResource(res);
    }

    /**
     * @return 类型名
     */
    public String getName() {
        return mType.getName();
    }

    /**
     * @return 类型图id
     */
    public int getRes() {
        return mType.getTypeId();
    }

    /**
     * 点击更换类型
     */
    public void click() {
        mBinding.getEdit().setType(mType);
        Animator appear = AnimatorInflater.loadAnimator(mContext, R.animator.type_appear);
        appear.setTarget(mBinding.typeImageView);
        appear.start();
    }
}
