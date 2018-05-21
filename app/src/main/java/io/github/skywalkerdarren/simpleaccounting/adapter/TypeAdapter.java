package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemTypeBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Type;
import io.github.skywalkerdarren.simpleaccounting.view_model.TypeItemViewModel;

/**
 * 类型适配器
 *
 * @author darren
 * @date 2018/3/8
 */

public class TypeAdapter extends BaseDataBindingAdapter<Type, ItemTypeBinding> {
    private FragmentBillEditBinding mBinding;

    public TypeAdapter(@Nullable List<Type> data, FragmentBillEditBinding binding) {
        super(R.layout.item_type, data);
        mBinding = binding;
    }

    @Override
    protected void convert(ItemTypeBinding binding, Type item) {
        binding.setType(new TypeItemViewModel(item, mBinding, mContext));
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        // 前20个项目需要动画延迟
        int num = 20;
        if (index < num) {
            int col = 4;
            int delay = index / col + index % col;
            anim.setStartDelay(delay * 75);
        }
        super.startAnim(anim, index);
    }
}
