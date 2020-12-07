package io.github.skywalkerdarren.simpleaccounting.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * @author darren
 * @date 2018/4/5
 */
abstract class BaseMultiItemDataBindingAdapter<T : MultiItemEntity, DB : ViewDataBinding>(data: List<T>?)
    : BaseMultiItemQuickAdapter<T, BaseDataBindingHolder<DB>>(data?.toMutableList()) {

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseDataBindingHolder<DB> {
        return BaseDataBindingHolder(LayoutInflater.from(context).inflate(layoutResId, parent, false))
    }
}