package io.github.skywalkerdarren.simpleaccounting.base

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseItemDraggableAdapter

/**
 * @author darren
 * @date 2018/4/9
 */
abstract class BaseDraggableDataBindingAdapter<T, Binding : ViewDataBinding>(layoutResId: Int, data: List<T>?)
    : BaseItemDraggableAdapter<T, BaseBindingViewHolder<Binding>>(layoutResId, data) {
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
        helper.binding?.let { convert(it, item) }
        helper.binding?.executePendingBindings()
    }

    protected abstract fun convert(binding: Binding, item: T)
}