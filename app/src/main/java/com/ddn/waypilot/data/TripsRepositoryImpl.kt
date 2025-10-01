package com.ddn.waypilot.data

import com.ddn.waypilot.data.local.TripsDao
import com.ddn.waypilot.data.mappers.toDomain
import com.ddn.waypilot.data.mappers.toRoomGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TripsRepositoryImpl @Inject constructor(
    private val dao: TripsDao
) : TripsRepository {

    override fun observeTrips(): Flow<List<Trip>> = flow {
        emit(dao.getTrips().map { it.toDomain() })
    }

    override suspend fun list(): List<Trip> =
        dao.getTrips().map { it.toDomain() }

    override suspend fun get(id: String): Trip? =
        dao.getTripById(id)?.toDomain()

    override suspend fun add(trip: Trip) {
        dao.insertTripWithRelations(trip.toRoomGraph())
    }

    override suspend fun delete(id: String) { // Existing method
        dao.deleteTrip(id)
    }

    override suspend fun deleteTrip(trip: Trip) { // New method implementation
        // Assuming Trip data class has an 'id' property of type String
        // to match the existing dao.deleteTrip(id: String)
        dao.deleteTrip(trip.id) 
    }
}
