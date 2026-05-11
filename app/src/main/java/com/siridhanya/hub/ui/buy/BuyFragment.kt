package com.siridhanya.hub.ui.buy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.databinding.FragmentBuyBinding
import com.siridhanya.hub.viewmodel.BuyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuyFragment : Fragment() {
    private var _b: FragmentBuyBinding? = null
    private val b get() = _b!!
    private val vm: BuyViewModel by viewModels()
    private lateinit var adapter: FpoAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentBuyBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FpoAdapter { phone ->
            try {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Phone: $phone", Toast.LENGTH_SHORT).show()
            }
        }
        b.rvFpo.layoutManager = LinearLayoutManager(requireContext())
        b.rvFpo.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.allFpo.collect { adapter.submitList(it) }
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
