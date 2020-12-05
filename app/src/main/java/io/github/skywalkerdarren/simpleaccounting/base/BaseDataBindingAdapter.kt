package io.github.skywalkerdarren.simpleaccounting.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * @author darren
 * @date 2018/4/5
 */
abstract class BaseDataBindingAdapter<T, DB : ViewDataBinding>(layoutResId: Int, data: List<T>?)
    : BaseQuickAdapter<T, BaseDataBindingHolder<DB>>(layoutResId, data?.toMutableList()) {

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseDataBindingHolder<DB> {
        return BaseDataBindingHolder(LayoutInflater.from(context).inflate(layoutResId, parent, false))
    }
}