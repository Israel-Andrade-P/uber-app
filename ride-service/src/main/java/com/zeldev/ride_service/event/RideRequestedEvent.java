package com.zeldev.ride_service.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequestedEvent {
    private String rideId;
    private String riderId;
    private double pickupLatitude;
    private double pickupLongitude;
    private String pickupAddress;
    private double dropOffLatitude;
    private double dropOffLongitude;
    private String dropOffAddress;
}
