package com.zeldev.location_service.dto;

import lombok.Builder;

@Builder
public record DriverLocationRequest(
        String driverId,
        double latitude,
        double longitude
) {}
