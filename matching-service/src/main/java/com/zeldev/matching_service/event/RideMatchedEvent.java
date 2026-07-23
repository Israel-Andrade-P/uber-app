package com.zeldev.matching_service.event;

import lombok.Builder;

@Builder
public record RideMatchedEvent(
        String rideId,
        String riderId,
        String driverId,
        double driverLatitude,
        double driverLongitude,
        double distanceFromPickUpKm
) {}
