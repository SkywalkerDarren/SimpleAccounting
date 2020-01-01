package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemTypeBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

/**
 * 类型适配器
 *
 * @author darren
 * @date 2018/3/8
 */

public class TypeAdapter extends BaseDataBindingAdapter<Type, ItemTypeBinding> {
    private FragmentBillEditBinding mBinding;
    public boolean isOpen = false;

    public TypeAdapter(FragmentBillEditBinding binding) {
        super(R.layout.item_type, null);
        mBinding = binding;
        openLoadAnimation(view -> {
            if (isOpen) {
                view.setAlpha(1);
                return new Animator[]{};
            }
            Animator animator = AnimatorInflater.loadAnimator(mContext,
                    R.animator.type_item_appear);
            animator.setTarget(view);
            return new Animator[]{animator};
        });
    }

    @Override
    protected void convert(ItemTypeBinding binding, Type item) {
        binding.typeItem.setAlpha(0);
        binding.setType(item);
        binding.circle.setOnClickListener(v -> click(item));
    }

    private void click(Type item) {
        isOpen = true;
        mBinding.getEdit().setType(item);
        Animator appear = AnimatorInflater.loadAnimator(mContext, R.animator.type_appear);
        appear.setTarget(mBinding.typeImageView);
        appear.start();
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
