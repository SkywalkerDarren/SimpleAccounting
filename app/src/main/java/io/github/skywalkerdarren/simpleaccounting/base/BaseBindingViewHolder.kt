package io.github.skywalkerdarren.simpleaccounting.base

import android.view.View
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author darren
 * @date 2018/4/5
 */
abstract class BaseBindingViewHolder<Binding : ViewDataBinding?> internal constructor(view: View?) : BaseViewHolder(view) {
    var binding: Binding? = null
        private set

    fun setBinding(binding: Binding) {
        this.binding = binding
    }
}