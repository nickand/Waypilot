package com.ddn.waypilot.data.local

import androidx.room.*
import com.ddn.waypilot.data.local.entities.*

@Dao
interface TripsDao {
    // INSERTS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Insert
    suspend fun insertFlights(flights: List<FlightSegmentEntity>)

    @Insert
    suspend fun insertHotels(hotels: List<HotelBookingEntity>)

    @Insert
    suspend fun insertActivities(activities: List<ActivityEntity>)

    @Insert
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    // TRANSACTION para insertar todo un viaje
    @Transaction
    suspend fun insertTripWithRelations(t: TripWithRelations) {
        insertTrip(t.trip)
        if (t.flights.isNotEmpty()) insertFlights(t.flights)
        if (t.hotels.isNotEmpty()) insertHotels(t.hotels)
        if (t.activities.isNotEmpty()) insertActivities(t.activities)
        if (t.restaurants.isNotEmpty()) insertRestaurants(t.restaurants)
    }

    // QUERIES
    @Transaction
    @Query("SELECT * FROM trips ORDER BY startDate ASC")
    suspend fun getTrips(): List<TripWithRelations>

    @Transaction
    @Query("SELECT * FROM trips WHERE id = :id LIMIT 1")
    suspend fun getTripById(id: String): TripWithRelations?

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTrip(id: String)
}
