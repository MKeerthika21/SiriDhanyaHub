package com.siridhanya.hub.viewmodel

import androidx.lifecycle.*
import com.siridhanya.hub.data.repository.SiriDhanyaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(private val repo: SiriDhanyaRepository) : ViewModel() {

    private val _selectedMillet = MutableStateFlow("All")
    val selectedMillet: StateFlow<String> = _selectedMillet

    val benefits = _selectedMillet.flatMapLatest { millet ->
        if (millet == "All") repo.getAllHealthBenefits()
        else repo.getHealthForMillet(millet)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setMillet(millet: String) { _selectedMillet.value = millet }
}
