package com.siridhanya.hub.ui.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.R
import com.siridhanya.hub.databinding.FragmentFavoritesBinding
import com.siridhanya.hub.ui.recipes.RecipeAdapter
import com.siridhanya.hub.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _b: FragmentFavoritesBinding? = null
    private val b get() = _b!!
    private val vm: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentFavoritesBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecipeAdapter(
            onRecipeClick = { recipe ->
                val bundle = Bundle().apply { putParcelable("recipe", recipe) }
                findNavController().navigate(R.id.action_favorites_to_recipeDetail, bundle)
            },
            onFavoriteClick = { recipe -> vm.toggleFavorite(recipe) }
        )
        b.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        b.rvFavorites.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.favoriteRecipes.collect { favorites ->
                    adapter.submitList(favorites)
                    b.tvEmpty.visibility     = if (favorites.isEmpty()) View.VISIBLE else View.GONE
                    b.rvFavorites.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
                    b.tvCount.text = "${favorites.size} saved recipes"
                }
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
