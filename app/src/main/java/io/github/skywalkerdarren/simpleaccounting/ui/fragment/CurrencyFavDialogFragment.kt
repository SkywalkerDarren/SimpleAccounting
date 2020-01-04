package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.AllCurrenciesAdapter
import io.github.skywalkerdarren.simpleaccounting.base.BaseDialogFragment
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDialogCurrencyFavBinding
import io.github.skywalkerdarren.simpleaccounting.view_model.CurrencyFavViewModel


class CurrencyFavDialogFragment : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_dialog_currency_fav, container, false)
        val binding = FragmentDialogCurrencyFavBinding.bind(root)
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
        return root
    }

}