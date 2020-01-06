package io.github.skywalkerdarren.simpleaccounting.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory
import io.github.skywalkerdarren.simpleaccounting.util.view.ScreenUtil

abstract class BaseDialogFragment : DialogFragment() {
    interface DismissListener {
        fun callback()
    }

    lateinit var onDismissListener: DismissListener
    lateinit var factory: ViewModelFactory

    fun setDismissListener(listener: DismissListener) {
        onDismissListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        factory = ViewModelFactory.getInstance(requireActivity().application)
        dialog?.apply {
            setCanceledOnTouchOutside(true)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        configWindowPercent(Gravity.CENTER, 0.8f, 0.8f)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.callback()
    }

    fun configWindowPercent(gravity: Int, widthPercent: Float, heightPercent: Float) {
        dialog?.window?.setGravity(gravity)
        val width = ScreenUtil.getScreenWidth(requireContext()) * widthPercent
        val height = ScreenUtil.getScreenHeight(requireContext()) * heightPercent
        dialog?.window?.setLayout(width.toInt(), height.toInt())

    }
}