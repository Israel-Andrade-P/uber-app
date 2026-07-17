package com.zeldev.ride_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RideRequest(
        @NotBlank(message = "Rider id is required")
        String riderId,
        @NotNull(message = "Pick up latitude is required")
        double pickupLatitude,
        @NotNull(message = "Pick up longitude is required")
        double pickupLongitude,
        @NotBlank(message = "Pick up address is required")
        String pickupAddress,
        @NotNull(message = "Drop off latitude is required")
        double dropOffLatitude,
        @NotNull(message = "Drop off longitude is required")
        double dropOffLongitude,
        @NotBlank(message = "Drop off address is required")
        String dropOffAddress
) {}
