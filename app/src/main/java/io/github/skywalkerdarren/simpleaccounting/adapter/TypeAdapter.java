package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.Animator;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Type;

/**
 * 类型适配器
 *
 * @author darren
 * @date 2018/3/8
 */

public class TypeAdapter extends BaseQuickAdapter<Type, BaseViewHolder> {
    public TypeAdapter(@Nullable List<Type> data) {
        super(R.layout.type_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Type item) {
        helper.setText(R.id.type_text_view, item.getName());
        helper.setImageResource(R.id.type_image_view, item.getTypeId());
        helper.addOnClickListener(R.id.circle);
        helper.setAlpha(R.id.type_item, 0);
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        // 前20个项目需要动画延迟
        if (index < 20) {
            int col = 4;
            int delay = index / col + index % col;
            anim.setStartDelay(delay * 75);
        }
        super.startAnim(anim, index);
    }
}
