package com.zeldev.ride_service.utils;

import com.zeldev.ride_service.event.RideRequestedEvent;
import com.zeldev.ride_service.model.Ride;

public class EventUtils {

    public static RideRequestedEvent createRideRequestedEvent(Ride ride) {
        return RideRequestedEvent.builder()
                .rideId(ride.getId())
                .riderId(ride.getRiderId())
                .pickupLatitude(ride.getPickupLatitude())
                .pickupLongitude(ride.getPickupLongitude())
                .pickupAddress(ride.getPickupAddress())
                .dropOffLatitude(ride.getDropOffLatitude())
                .dropOffLongitude(ride.getDropOffLongitude())
                .dropOffAddress(ride.getDropOffAddress())
                .build();
    }
}
