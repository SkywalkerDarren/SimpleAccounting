package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.Animator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.DefaultDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemExchangeRateBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.view_model.DiscoveryViewModel

class ExchangeRateAdapter(private val viewModel: DiscoveryViewModel) :
        BaseDraggableDataBindingAdapter<CurrencyAndInfo, ItemExchangeRateBinding>(R.layout.item_exchange_rate, null) {
    var isDrag = false
        private set

    override fun convert(holder: BaseDataBindingHolder<ItemExchangeRateBinding>, item: CurrencyAndInfo) {
        holder.dataBinding?.data = item.currency
        holder.dataBinding?.info = item.currencyInfo
    }

    override fun startAnim(anim: Animator, index: Int) {
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
    }

    fun setNewList(currencies: List<CurrencyAndInfo>) {
        setDiffNewData(currencies.toMutableList())
    }

    init {
        setDiffCallback(DefaultDiff())
        draggableModule.setOnItemDragListener(object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                isDrag = true
            }

            override fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int) {
                viewModel.changeCurrency(getItem(from).currency, getItem(to).currency)
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                isDrag = false
            }
        })
    }
}