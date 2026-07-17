package com.zeldev.ride_service.model;

import com.zeldev.ride_service.enumeration.RideStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String riderId;
    @Column(nullable = false)
    private String driverId;
    @Column(nullable = false)
    private double pickupLatitude;
    @Column(nullable = false)
    private double pickupLongitude;
    @Column(nullable = false)
    private String pickupAddress;
    @Column(nullable = false)
    private double dropOffLatitude;
    @Column(nullable = false)
    private double dropOffLongitude;
    @Column(nullable = false)
    private String dropOffAddress;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;
    private double estimatedFare;
    private double actualFare;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime rideStartedAt;
    private LocalDateTime rideFinishedAt;
}
