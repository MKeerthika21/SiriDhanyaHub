package com.siridhanya.hub.viewmodel

import androidx.lifecycle.*
import com.siridhanya.hub.data.repository.SiriDhanyaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BuyViewModel @Inject constructor(private val repo: SiriDhanyaRepository) : ViewModel() {
    val allFpo = repo.getAllFpo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
