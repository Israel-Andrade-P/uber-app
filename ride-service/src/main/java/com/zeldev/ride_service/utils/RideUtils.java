package com.zeldev.ride_service.utils;

import com.zeldev.ride_service.dto.RideRequest;
import com.zeldev.ride_service.dto.RideResponse;
import com.zeldev.ride_service.model.Ride;

import static com.zeldev.ride_service.enumeration.RideStatus.REQUESTED;
import static java.lang.Math.*;

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

    private static double calculateEstimatedFare(RideRequest request) {
        //simplified haversine distance calculation
        double lat1 = toRadians(request.pickupLatitude());
        double lat2 = toRadians(request.dropOffLatitude());

        double lon1 = toRadians(request.pickupLongitude());
        double lon2 = toRadians(request.dropOffLongitude());

        double diffLat = lat2 - lat1;
        double diffLon = lon2 - lon1;

        double calcResult = pow(sin(diffLat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(diffLon / 2), 2);

        double calcResult2 = 2 * asin(sqrt(calcResult));
        double distanceInKm = 6371 * calcResult2;

        //base fare: R$5,00 + R$2,00 per km
        double fare = 5 + (distanceInKm * 2);

        return round(fare * 100.0) / 100.0;
    }
}
