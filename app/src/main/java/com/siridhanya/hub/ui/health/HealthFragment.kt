package com.siridhanya.hub.ui.health

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.data.entities.MilletType
import com.siridhanya.hub.databinding.FragmentHealthBinding
import com.siridhanya.hub.viewmodel.HealthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HealthFragment : Fragment() {
    private var _b: FragmentHealthBinding? = null
    private val b get() = _b!!
    private val vm: HealthViewModel by viewModels()
    private lateinit var adapter: HealthAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHealthBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HealthAdapter()
        b.rvHealth.layoutManager = LinearLayoutManager(requireContext())
        b.rvHealth.adapter = adapter

        val chips = mapOf(
            b.chipAllH   to "All",
            b.chipNavaneH to MilletType.FOXTAIL.name,
            b.chipRagiH  to MilletType.FINGER.name,
            b.chipSajjeH to MilletType.PEARL.name,
            b.chipJowarH to MilletType.SORGHUM.name
        )
        chips.forEach { (chip, millet) ->
            chip.setOnClickListener {
                chips.keys.forEach { it.isChecked = false }
                chip.isChecked = true
                vm.setMillet(millet)
            }
        }
        b.chipAllH.isChecked = true

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.benefits.collect { adapter.submitList(it) }
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
