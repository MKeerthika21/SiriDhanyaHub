package com.siridhanya.hub.ui.mandi

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.databinding.FragmentMandiBinding
import com.siridhanya.hub.viewmodel.MandiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MandiFragment : Fragment() {
    private var _b: FragmentMandiBinding? = null
    private val b get() = _b!!
    private val vm: MandiViewModel by viewModels()
    private lateinit var adapter: MandiAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentMandiBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MandiAdapter()
        b.rvMandi.layoutManager = LinearLayoutManager(requireContext())
        b.rvMandi.adapter = adapter
        b.swipeRefresh.setOnRefreshListener { vm.refreshPrices(); b.swipeRefresh.isRefreshing = false }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.allCities.collect { cities ->
                        val all = mutableListOf("All") + cities
                        val sp = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, all)
                        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        b.spinnerCity.adapter = sp
                        b.spinnerCity.setSelection(0)
                    }
                }
                launch {
                    vm.prices.collect { prices ->
                        adapter.submitList(prices)
                        b.tvPriceCount.text = "${prices.size} prices found"
                    }
                }
            }
        }
        b.spinnerCity.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                vm.selectCity(b.spinnerCity.selectedItem?.toString() ?: "All")
            }
            override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
        })
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
