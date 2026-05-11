package com.siridhanya.hub.ui.recipes

import android.os.Bundle
import android.text.*
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.siridhanya.hub.R
import com.siridhanya.hub.data.entities.MilletType
import com.siridhanya.hub.databinding.FragmentRecipesBinding
import com.siridhanya.hub.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _b: FragmentRecipesBinding? = null
    private val b get() = _b!!
    private val vm: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentRecipesBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecipeAdapter(
            onRecipeClick = { recipe ->
                val bundle = Bundle().apply { putParcelable("recipe", recipe) }
                findNavController().navigate(R.id.action_recipes_to_recipeDetail, bundle)
            },
            onFavoriteClick = { recipe -> vm.toggleFavorite(recipe) }
        )
        b.rvRecipes.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvRecipes.adapter = adapter

        // Search
        b.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { vm.setSearch(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b2: Int, c: Int) {}
        })

        // Millet filter chips
        setupFilterChips()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.recipes.collect { recipes ->
                    adapter.submitList(recipes)
                    b.tvCount.text = "${recipes.size} recipes"
                    b.tvEmpty.visibility = if (recipes.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun setupFilterChips() {
        val chips = mapOf(
            b.chipAll      to "All",
            b.chipNavane   to MilletType.FOXTAIL.name,
            b.chipSajje    to MilletType.PEARL.name,
            b.chipRagi     to MilletType.FINGER.name,
            b.chipJowar    to MilletType.SORGHUM.name
        )
        chips.forEach { (chip, millet) ->
            chip.setOnClickListener {
                chips.keys.forEach { it.isChecked = false }
                chip.isChecked = true
                vm.setMillet(millet)
            }
        }
        b.chipAll.isChecked = true
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
