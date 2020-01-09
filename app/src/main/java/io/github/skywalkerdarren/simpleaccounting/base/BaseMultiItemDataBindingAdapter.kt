package io.github.skywalkerdarren.simpleaccounting.base

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author darren
 * @date 2018/4/5
 */
abstract class BaseMultiItemDataBindingAdapter<T : MultiItemEntity, Binding : ViewDataBinding>(data: List<T>?)
    : BaseMultiItemQuickAdapter<T, BaseBindingViewHolder<Binding>>(data) {
    override fun createBaseViewHolder(view: View): BaseBindingViewHolder<Binding> {
        return object : BaseBindingViewHolder<Binding>(view) {}
    }

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseBindingViewHolder<Binding> {
        val binding: Binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false)
        val view: View
        view = binding.root
        val holder: BaseBindingViewHolder<Binding> = object : BaseBindingViewHolder<Binding>(view) {}
        holder.setBinding(binding)
        return holder
    }

    override fun convert(helper: BaseBindingViewHolder<Binding>, item: T) {
        convert(helper.binding, item)
        helper.binding.executePendingBindings()
    }

    protected abstract fun convert(binding: Binding, item: T)
}