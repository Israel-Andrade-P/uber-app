package com.zeldev.matching_service.utils;

import com.zeldev.matching_service.dto.NearbyDriverResponse;
import com.zeldev.matching_service.event.RideMatchedEvent;
import com.zeldev.matching_service.event.RideRequestedEvent;

public class EventUtils {

    public static RideMatchedEvent createRideMatchedEvent(RideRequestedEvent event, NearbyDriverResponse driver) {
        return RideMatchedEvent.builder()
                .rideId(event.rideId())
                .riderId(event.riderId())
                .driverId(driver.driverId())
                .driverLatitude(driver.latitude())
                .driverLongitude(driver.longitude())
                .distanceFromPickUpKm(driver.distanceInKm())
                .build();
    }
}
