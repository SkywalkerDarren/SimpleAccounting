package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.AllCurrenciesAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDialogCurrencyFavBinding
import io.github.skywalkerdarren.simpleaccounting.util.ScreenUtil
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory
import io.github.skywalkerdarren.simpleaccounting.view_model.CurrencyFavViewModel


class CurrencyFavDialogFragment : DialogFragment() {
    interface Listener {
        fun callback()
    }

    lateinit var onDismissListener: Listener

    fun setDismissListener(listener: Listener) {
        onDismissListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_dialog_currency_fav, container, false)
        val binding = FragmentDialogCurrencyFavBinding.bind(root)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        val viewModel = ViewModelProviders.of(this, factory).get(CurrencyFavViewModel::class.java)
        val adapter = AllCurrenciesAdapter(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        viewModel.getCurrencies().observe(viewLifecycleOwner, Observer {
            adapter.setNewData(it)
        })
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.start()
        dialog?.apply {
            setCanceledOnTouchOutside(true)
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        configWindowPercent(Gravity.CENTER, 0.8f, 0.8f)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.callback()
    }

    private fun configWindowPercent(gravity: Int, widthPercent: Float, heightPercent: Float) {
        dialog?.window?.setGravity(gravity)
        val width = ScreenUtil.getScreenWidth(requireContext()) * widthPercent
        val height = ScreenUtil.getScreenHeight(requireContext()) * heightPercent
        dialog?.window?.setLayout(width.toInt(), height.toInt())

    }

}