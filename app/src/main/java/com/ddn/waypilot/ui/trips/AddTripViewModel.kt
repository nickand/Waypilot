package com.ddn.waypilot.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddn.waypilot.data.Budget
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.TripStyle
import com.ddn.waypilot.data.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddTripViewModel @Inject constructor(
    private val repo: TripsRepository
) : ViewModel() {

    fun addBasicTrip(
        title: String,
        city: String,
        start: LocalDate,
        end: LocalDate,
        travelers: Int,
        style: TripStyle,  // <- FQN aquí
        budgetCurrency: String?,
        budgetTotal: Double?,
        coverImageUrl: String?,
        onDone: () -> Unit) {
        viewModelScope.launch {
            val trip = Trip(
                id = "t-" + UUID.randomUUID().toString().take(8),
                title = title,
                destinationCity = city,
                startDate = start,
                endDate = end,
                travelersCount = travelers,
                style = style,
                coverImageUrl = coverImageUrl,   // <-- se asigna aquí
                budget = if (budgetCurrency != null || budgetTotal != null)
                    Budget(budgetCurrency ?: "USD", budgetTotal ?: 0.0) else null,
                flights = emptyList(),
                hotels = emptyList(),
                activities = emptyList(),
                restaurants = emptyList(),
                notes = null
            )
            repo.add(trip)
            onDone()
        }
    }
}
