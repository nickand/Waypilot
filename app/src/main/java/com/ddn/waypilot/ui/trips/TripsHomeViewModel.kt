package com.ddn.waypilot.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsHomeViewModel @Inject constructor(
    private val repo: TripsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<List<Trip>>(emptyList())
    val state: StateFlow<List<Trip>> = _state

    fun load() {
        viewModelScope.launch {
            _state.value = repo.list()
        }
    }
}
