package io.github.skywalkerdarren.simpleaccounting.base

import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.module.DraggableModule

/**
 * @author darren
 * @date 2018/4/9
 */
abstract class BaseDraggableDataBindingAdapter<T, DB : ViewDataBinding>(layoutResId: Int, data: List<T>?)
    : BaseDataBindingAdapter<T, DB>(layoutResId, data), DraggableModule