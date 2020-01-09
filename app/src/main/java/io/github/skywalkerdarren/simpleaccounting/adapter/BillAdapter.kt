package io.github.skywalkerdarren.simpleaccounting.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.BillInfoDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillBinding
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillHeaderBinding
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillWithoutRemarkBinding
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository.Companion.getInstance
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillDetailActivity
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors

/**
 * 账单适配器
 * 将帐单列表适配到recycler view
 *
 * @author darren
 * @date 2018/2/12
 */
class BillAdapter(bills: List<BillInfo>?, private val mActivity: FragmentActivity)
    : BaseMultiItemDataBindingAdapter<BillInfo, ViewDataBinding>(bills) {
    private val mRepository: AppRepository? = getInstance(AppExecutors(), mActivity)
    private var mX = 0
    private var mY = 0
    fun setNewList(data: List<BillInfo>?) {
        setNewDiffData(BillInfoDiff(data))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun convert(binding: ViewDataBinding, item: BillInfo) {
        when (binding) {
            is ItemListBillBinding -> {
                binding.billInfo = item
                val imageView = binding.typeImageView
                binding.contentCardView.setOnClickListener { click(item, imageView) }
                binding.contentCardView.setOnTouchListener { _: View?, event: MotionEvent ->
                    touch(event)
                    false
                }
            }
            is ItemListBillWithoutRemarkBinding -> {
                binding.billInfo = item
                val imageView = binding.typeImageView
                binding.contentCardView.setOnClickListener { click(item, imageView) }
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

    private fun touch(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mX = event.rawX.toInt()
            mY = event.rawY.toInt()
        }
    }

    private fun click(item: BillInfo, imageView: ImageView) {
        mRepository?.getBill(item.uuid ?: return, object : LoadBillCallBack {
            override fun onBillLoaded(bill: Bill?) {
                val intent = BillDetailActivity.newIntent(mContext,
                        bill, mX, mY, R.color.orangea200)
                intent.putExtra(BillDetailActivity.EXTRA_START_COLOR, R.color.orangea200)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mActivity, imageView, "type_image_view")
                mContext.startActivity(intent, options.toBundle())
            }
        })
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
        addItemType(WITH_REMARK, R.layout.item_list_bill)
        addItemType(WITHOUT_REMARK, R.layout.item_list_bill_without_remark)
        addItemType(HEADER, R.layout.item_list_bill_header)
    }
}