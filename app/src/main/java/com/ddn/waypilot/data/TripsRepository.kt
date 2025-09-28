package com.ddn.waypilot.data

import kotlinx.coroutines.flow.Flow

interface TripsRepository {
    fun observeTrips(): Flow<List<Trip>>
    suspend fun list(): List<Trip>
    suspend fun get(id: String): Trip?
    suspend fun add(trip: Trip)
    suspend fun delete(id: String)
}
