package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.CurrencySelectAdapter
import io.github.skywalkerdarren.simpleaccounting.base.BaseDialogFragment
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDialogCurrencySelectBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import io.github.skywalkerdarren.simpleaccounting.view_model.CurrencySelectViewModel


class CurrencySelectDialogFragment : BaseDialogFragment() {
    val currencyAdapter = CurrencySelectAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_dialog_currency_select, container, false)
        val binding = FragmentDialogCurrencySelectBinding.bind(root)
        val viewModel = ViewModelProviders.of(this, factory).get(CurrencySelectViewModel::class.java)
        currencyAdapter.setOnItemChildClickListener { adapter, _, position ->
            currencyAdapter.current = (adapter.data[position] as CurrencyInfo).name
            currencyAdapter.notifyDataSetChanged()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = currencyAdapter
        viewModel.getCurrencies().observe(viewLifecycleOwner, Observer {
            currencyAdapter.setNewData(it)
        })
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return root
    }

}