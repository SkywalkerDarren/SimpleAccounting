package io.github.skywalkerdarren.simpleaccounting.adapter

import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.AccountDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemAccountBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel

/**
 * @author darren
 * @date 2018/3/24
 */
class AccountAdapter(private val viewModel: AccountViewModel) : BaseDraggableDataBindingAdapter<Account, ItemAccountBinding>(R.layout.item_account, null) {
    private fun itemRaiseAnimator(view: View, start: Float, raise: Boolean) {
        val end = start * 2
        val animator = ObjectAnimator.ofFloat(view, "elevation",
                if (raise) start else end, if (raise) end else start)
        animator.duration = 50
        animator.start()
    }

    override fun convert(binding: ItemAccountBinding, item: Account) {
        binding.account = item
    }

    fun setNewList(accounts: List<Account>?) {
        setNewDiffData(AccountDiff(accounts))
    }

    init {
        setOnItemDragListener(object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                val st = viewHolder.itemView.elevation
                itemRaiseAnimator(viewHolder.itemView, st, true)
            }

            override fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int) {
                viewModel.changePosition(getItem(from) ?: return, getItem(to) ?: return)
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                val ed = viewHolder.itemView.elevation
                itemRaiseAnimator(viewHolder.itemView, ed, false)
            }
        })
    }
}