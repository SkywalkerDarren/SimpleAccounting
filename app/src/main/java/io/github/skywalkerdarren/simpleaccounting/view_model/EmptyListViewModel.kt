package io.github.skywalkerdarren.simpleaccounting.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity

/**
 * 空列表vm
 *
 * @author darren
 * @date 2018/4/5
 */
class EmptyListViewModel : ViewModel() {
    /**
     * 点击跳转新建账单
     */
    fun onClick(context: Context) {
        val intent = BillEditActivity.newIntent(context, null,
                mX, mY)
        context.startActivity(intent)
    }

    companion object {
        private var mX = 0
        private var mY = 0
        /**
         * 获取位置
         */
        @JvmStatic
        @SuppressLint("ClickableViewAccessibility")
        @BindingAdapter("onTouch")
        fun setTouchListener(view: View, b: Boolean) {
            view.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    mX = motionEvent.rawX.toInt()
                    mY = motionEvent.rawY.toInt()
                }
                b
            }
        }
    }
}