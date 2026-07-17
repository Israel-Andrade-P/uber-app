package com.zeldev.ride_service.utils;

import com.zeldev.ride_service.dto.RideRequest;
import com.zeldev.ride_service.dto.RideResponse;
import com.zeldev.ride_service.model.Ride;

import static com.zeldev.ride_service.enumeration.RideStatus.REQUESTED;

public class RideUtils {

    public static Ride toRide(RideRequest request) {
        return Ride.builder()
                .riderId(request.riderId())
                .pickupLatitude(request.pickupLatitude())
                .pickupLongitude(request.pickupLongitude())
                .pickupAddress(request.pickupAddress())
                .dropOffLatitude(request.dropOffLatitude())
                .dropOffLongitude(request.dropOffLongitude())
                .dropOffAddress(request.dropOffAddress())
                .status(REQUESTED)
                .estimatedFare(calculateEstimatedFare(request))
                .build();
    }

    public static RideResponse toRideResponse(Ride ride) {
        return RideResponse.builder()
                .id(ride.getId())
                .riderId(ride.getRiderId())
                .driverId(ride.getDriverId())
                .pickupLatitude(ride.getPickupLatitude())
                .pickupLongitude(ride.getPickupLongitude())
                .pickupAddress(ride.getPickupAddress())
                .dropOffLatitude(ride.getDropOffLatitude())
                .dropOffLongitude(ride.getDropOffLongitude())
                .dropOffAddress(ride.getDropOffAddress())
                .status(ride.getStatus())
                .estimatedFare(ride.getEstimatedFare())
                .actualFare(ride.getActualFare())
                .createdAt(ride.getCreatedAt())
                .updatedAt(ride.getUpdatedAt())
                .rideStartedAt(ride.getRideStartedAt())
                .rideFinishedAt(ride.getRideFinishedAt())
                .build();
    }
}
