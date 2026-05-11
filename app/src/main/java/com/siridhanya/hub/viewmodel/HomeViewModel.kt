package com.siridhanya.hub.viewmodel

import androidx.lifecycle.*
import com.siridhanya.hub.data.repository.SiriDhanyaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: SiriDhanyaRepository) : ViewModel() {
    val topPrices    = repo.getAllPrices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val featuredRecipes = repo.getAllRecipes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seed() { viewModelScope.launch { repo.seedIfEmpty() } }
}
