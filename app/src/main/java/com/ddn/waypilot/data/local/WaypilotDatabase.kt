package com.ddn.waypilot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ddn.waypilot.data.local.entities.*

@Database(
    entities = [
        TripEntity::class,
        FlightSegmentEntity::class,
        HotelBookingEntity::class,
        ActivityEntity::class,
        RestaurantEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WaypilotDatabase : RoomDatabase() {
    abstract fun tripsDao(): TripsDao
}
