// com/ddn/waypilot/data/mappers/TripMappers.kt
package com.ddn.waypilot.data.mappers

// ==== IMPORTS DEL DOMINIO (alias para evitar choques) ====
import com.ddn.waypilot.data.ActivityItem
import com.ddn.waypilot.data.Budget
import com.ddn.waypilot.data.Destination
import com.ddn.waypilot.data.FlightSegment
import com.ddn.waypilot.data.HotelBooking
import com.ddn.waypilot.data.RestaurantReservation
import com.ddn.waypilot.data.Trip
import com.ddn.waypilot.data.Trip as DomainTrip
import com.ddn.waypilot.data.Budget as DomainBudget
import com.ddn.waypilot.data.FlightSegment as DomainFlightSegment
import com.ddn.waypilot.data.HotelBooking as DomainHotelBooking
import com.ddn.waypilot.data.ActivityItem as DomainActivityItem
import com.ddn.waypilot.data.RestaurantReservation as DomainRestaurantReservation
import com.ddn.waypilot.data.TripStyle as DomainTripStyle
import com.ddn.waypilot.data.ActivityCategory as DomainActivityCategory

// ==== IMPORTS DE ROOM ====
import com.ddn.waypilot.data.local.entities.*

/* ------------------ Domain -> Room ------------------ */

fun DomainTrip.toEntity(): TripEntity =
    TripEntity(
        id = id,
        title = title,
        destinationCity = destinationCity,
        startDate = startDate,
        endDate = endDate,
        travelersCount = travelersCount,
        style = style?.name,                 // guardamos el nombre del enum
        coverImageUrl = coverImageUrl,
        currencyCode = budget?.currencyCode,
        estimatedTotal = budget?.estimatedTotal,
        notes = notes
    )

fun DomainFlightSegment.toEntity(tripId: String) = FlightSegmentEntity(
    tripId = tripId,
    originCode = originCode,
    destinationCode = destinationCode,
    departure = departure,
    arrival = arrival,
    airline = airline,
    flightNumber = flightNumber
)

fun DomainHotelBooking.toEntity(tripId: String) = HotelBookingEntity(
    tripId = tripId,
    name = name,
    address = address,
    checkIn = checkIn,
    checkOut = checkOut,
    confirmationCode = confirmationCode
)

fun DomainActivityItem.toEntity(tripId: String) = ActivityEntity(
    tripId = tripId,
    title = title,
    start = start,
    end = end,
    location = location,
    category = category?.name
)

fun DomainRestaurantReservation.toEntity(tripId: String) = RestaurantEntity(
    tripId = tripId,
    name = name,
    time = time,
    address = address,
    confirmationCode = confirmationCode
)

// === NUEVO: destinos ===
fun Destination.toEntity(tripId: String, orderIndex: Int) = DestinationEntity(
    tripId = tripId,
    orderIndex = orderIndex,
    placeId = placeId,
    name = name,
    address = address,
    lat = lat,
    lng = lng,
    notes = notes
)

fun DomainTrip.toRoomGraph(): TripWithRelations =
    TripWithRelations(
        trip = toEntity(),
        flights = flights.map { it.toEntity(id) },
        hotels = hotels.map { it.toEntity(id) },
        activities = activities.map { it.toEntity(id) },
        restaurants = restaurants.map { it.toEntity(id) },
        destinations = destinations.mapIndexed { idx, d -> d.toEntity(id, idx) }
    )

/* ------------------ Room -> Domain ------------------ */

fun TripWithRelations.toDomain(): Trip =
    Trip(
        id = trip.id,
        title = trip.title,
        destinationCity = trip.destinationCity,
        startDate = trip.startDate,
        endDate = trip.endDate,
        travelersCount = trip.travelersCount,
        style = trip.style
            ?.let { name -> DomainTripStyle.valueOf(name) }
            ?: DomainTripStyle.SOLO,
        coverImageUrl = trip.coverImageUrl,
        budget = if (trip.currencyCode != null || trip.estimatedTotal != null)
            Budget(trip.currencyCode ?: "USD", trip.estimatedTotal ?: 0.0)
        else null,
        flights = flights.map {
            FlightSegment(
                originCode = it.originCode,
                destinationCode = it.destinationCode,
                departure = it.departure,
                arrival = it.arrival,
                airline = it.airline,
                flightNumber = it.flightNumber
            )
        },
        hotels = hotels.map {
            HotelBooking(
                name = it.name,
                address = it.address,
                checkIn = it.checkIn,
                checkOut = it.checkOut,
                confirmationCode = it.confirmationCode
            )
        },
        activities = activities.map {
            ActivityItem(
                title = it.title,
                start = it.start,
                end = it.end,
                location = it.location,
                category = it.category?.let { c ->
                    DomainActivityCategory.valueOf(c)
                } ?: DomainActivityCategory.OTHER,
                notes = null
            )
        },
        restaurants = restaurants.map {
            RestaurantReservation(
                name = it.name,
                time = it.time,
                address = it.address,
                confirmationCode = it.confirmationCode
            )
        },
        notes = trip.notes,
        // === NUEVO: destinos ===
        destinations = destinations.sortedBy { it.orderIndex }.map { e ->
            Destination(
                placeId = e.placeId,
                name = e.name,
                address = e.address,
                lat = e.lat,
                lng = e.lng,
                notes = e.notes
            )
        }
    )
