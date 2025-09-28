package com.ddn.waypilot.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.TripsRepository
import com.ddn.waypilot.nav.Dest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItineraryDetailViewModel @Inject constructor(
    private val repo: TripsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // estado simple: el viaje (null mientras carga)
    var trip: Trip? = null
        private set

    init {
        val id = checkNotNull(savedStateHandle.get<String>(Dest.ItineraryDetail.ARG_TRIP_ID))
        viewModelScope.launch {
            trip = repo.get(id)
            // Si usas Compose State, puedes migrar a StateFlow/MutableStateFlow
        }
    }
}
