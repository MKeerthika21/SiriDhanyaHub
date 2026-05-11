package com.siridhanya.hub.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.R
import com.siridhanya.hub.databinding.FragmentHomeBinding
import com.siridhanya.hub.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHomeBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.btnGoMandi.setOnClickListener   { findNavController().navigate(R.id.action_home_to_mandi) }
        b.btnGoRecipes.setOnClickListener { findNavController().navigate(R.id.action_home_to_recipes) }
        b.btnGoHealth.setOnClickListener  { findNavController().navigate(R.id.action_home_to_health) }
        b.btnGoBuy.setOnClickListener     { findNavController().navigate(R.id.action_home_to_buy) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.topPrices.collect { prices ->
                        if (prices.isNotEmpty()) {
                            val top = prices.take(3)
                            b.tvPrice1.text = "🌾 ${top.getOrNull(0)?.let { "${it.milletType.take(4)} - ₹${it.pricePerKg}/kg ${it.trendEmoji()}" } ?: ""}"
                            b.tvPrice2.text = "🌿 ${top.getOrNull(1)?.let { "${it.milletType.take(4)} - ₹${it.pricePerKg}/kg ${it.trendEmoji()}" } ?: ""}"
                            b.tvPrice3.text = "🌱 ${top.getOrNull(2)?.let { "${it.milletType.take(4)} - ₹${it.pricePerKg}/kg ${it.trendEmoji()}" } ?: ""}"
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
