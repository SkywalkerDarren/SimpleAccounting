package io.github.skywalkerdarren.simpleaccounting.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author darren
 * @date 2018/4/5
 */

public abstract class BaseDataBindingAdapter<T, Binding extends ViewDataBinding>
        extends BaseQuickAdapter<T, BaseBindingViewHolder<Binding>> {
    public BaseDataBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected BaseBindingViewHolder<Binding> createBaseViewHolder(View view) {
        return new BaseBindingViewHolder<>(view);
    }

    @Override
    protected BaseBindingViewHolder<Binding> createBaseViewHolder(ViewGroup parent, int layoutResId) {
        Binding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        View view;

        if (binding == null) {
            view = getItemView(layoutResId, parent);
        } else {
            view = binding.getRoot();
        }
        BaseBindingViewHolder<Binding> holder = new BaseBindingViewHolder<>(view);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    protected void convert(BaseBindingViewHolder<Binding> helper, T item) {
        convert(helper.getBinding(), item);
        helper.getBinding().executePendingBindings();
    }

    protected abstract void convert(Binding binding, T item);
}
