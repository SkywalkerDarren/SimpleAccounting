package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.Animator
import android.app.Application
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemExchangeRateBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory
import io.github.skywalkerdarren.simpleaccounting.view_model.DiscoveryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeRateAdapter(application: Application, private val viewModel: DiscoveryViewModel) :
        BaseDraggableDataBindingAdapter<CurrencyAndInfo, ItemExchangeRateBinding>(R.layout.item_exchange_rate, null) {
    private val mRepository: CurrencyRepo = ViewModelFactory.getInstance(application).repository

    override fun convert(binding: ItemExchangeRateBinding, item: CurrencyAndInfo) {
        binding.data = item.currency
        binding.info = item.currencyInfo
    }

    override fun startAnim(anim: Animator, index: Int) {
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
    }

    fun setNewList(currencies: List<CurrencyAndInfo>) {
        setNewDiffData(CurrencyAndInfoDiff(currencies))
    }

    init {
        setOnItemDragListener(object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {}
            override fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int) {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        mRepository.changeCurrencyPosition(
                                getItem(from)?.currency ?: return@withContext,
                                getItem(to)?.currency ?: return@withContext)
                    }
                }
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {}
        })
    }
}