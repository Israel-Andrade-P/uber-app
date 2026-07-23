package com.zeldev.matching_service.event;

public record RideRequestedEvent(
        String rideId,
        String riderId,
        double pickupLatitude,
        double pickupLongitude,
        String pickupAddress,
        double dropOffLatitude,
        double dropOffLongitude,
        String dropOffAddress
) {}
