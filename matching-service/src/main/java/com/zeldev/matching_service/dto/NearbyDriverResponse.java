package com.zeldev.matching_service.dto;

public record NearbyDriverResponse(
        String driverId,
        double latitude,
        double longitude,
        double distanceInKm
) {}
