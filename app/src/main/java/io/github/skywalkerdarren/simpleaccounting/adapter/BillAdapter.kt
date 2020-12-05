package io.github.skywalkerdarren.simpleaccounting.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.DefaultDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillBinding
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillHeaderBinding
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillWithoutRemarkBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo

/**
 * 账单适配器
 * 将帐单列表适配到recycler view
 *
 * @author darren
 * @date 2018/2/12
 */
class BillAdapter(bills: List<BillInfo>?)
    : BaseMultiItemDataBindingAdapter<BillInfo, ViewDataBinding>(bills) {
    var mX = 0
        private set
    var mY = 0
        private set
    fun setNewList(data: List<BillInfo>?) {
        setDiffNewData(data?.toMutableList())
    }

    var listener: OnClickListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun convert(holder: BaseDataBindingHolder<ViewDataBinding>, item: BillInfo) {
        when (val binding = holder.dataBinding) {
            is ItemListBillBinding -> {
                binding.billInfo = item
                val imageView = binding.typeImageView
                binding.contentCardView.setOnClickListener { listener?.click(item, imageView) }
                binding.contentCardView.setOnTouchListener { _: View?, event: MotionEvent ->
                    touch(event)
                    false
                }
            }
            is ItemListBillWithoutRemarkBinding -> {
                binding.billInfo = item
                val imageView = binding.typeImageView
                binding.contentCardView.setOnClickListener { listener?.click(item, imageView) }
                binding.contentCardView.setOnTouchListener { _: View?, event: MotionEvent ->
                    touch(event)
                    false
                }
            }
            is ItemListBillHeaderBinding -> {
                binding.billInfo = item
            }
        }
    }

    interface OnClickListener {
        fun click(item: BillInfo, imageView: ImageView)
    }

    private fun touch(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mX = event.rawX.toInt()
            mY = event.rawY.toInt()
        }
    }

    companion object {
        /**
         * 分隔符
         */
        const val HEADER = 2
        /**
         * 不带备注的账单
         */
        const val WITHOUT_REMARK = 0
        /**
         * 带备注的账单
         */
        const val WITH_REMARK = 1
    }

    init {
        setDiffCallback(DefaultDiff())
        addItemType(WITH_REMARK, R.layout.item_list_bill)
        addItemType(WITHOUT_REMARK, R.layout.item_list_bill_without_remark)
        addItemType(HEADER, R.layout.item_list_bill_header)
    }
}