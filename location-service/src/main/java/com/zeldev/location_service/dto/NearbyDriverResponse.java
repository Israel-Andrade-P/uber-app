package com.zeldev.location_service.dto;

import lombok.Builder;

@Builder
public record NearbyDriverResponse(
        String driverId,
        double latitude,
        double longitude,
        double distanceInKm
) {}
