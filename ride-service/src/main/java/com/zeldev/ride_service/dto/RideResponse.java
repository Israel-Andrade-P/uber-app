package com.zeldev.ride_service.dto;

import com.zeldev.ride_service.enumeration.RideStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RideResponse(
        String id,
        String riderId,
        String driverId,
        double pickupLatitude,
        double pickupLongitude,
        String pickupAddress,
        double dropOffLatitude,
        double dropOffLongitude,
        String dropOffAddress,
        RideStatus status,
        double estimatedFare,
        double actualFare,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime rideStartedAt,
        LocalDateTime rideFinishedAt
) {}
