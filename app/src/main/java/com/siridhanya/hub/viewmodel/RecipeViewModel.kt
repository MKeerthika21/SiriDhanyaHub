package com.siridhanya.hub.viewmodel

import androidx.lifecycle.*
import com.siridhanya.hub.data.entities.Recipe
import com.siridhanya.hub.data.repository.SiriDhanyaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repo: SiriDhanyaRepository) : ViewModel() {

    private val _searchQuery   = MutableStateFlow("")
    private val _selectedMillet = MutableStateFlow("All")

    val searchQuery: StateFlow<String>    = _searchQuery
    val selectedMillet: StateFlow<String> = _selectedMillet

    val recipes: StateFlow<List<Recipe>> = combine(_searchQuery, _selectedMillet) { q, millet ->
        Pair(q, millet)
    }.flatMapLatest { (q, millet) ->
        when {
            q.isNotBlank()     -> repo.searchRecipes(q)
            millet != "All"    -> repo.getRecipesByMillet(millet)
            else               -> repo.getAllRecipes()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteRecipes = repo.getFavoriteRecipes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearch(q: String)       { _searchQuery.value = q }
    fun setMillet(millet: String)  { _selectedMillet.value = millet }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch { repo.toggleFavorite(recipe) }
    }
}
