package com.zeldev.ride_service.repository;

import com.zeldev.ride_service.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, String> {
}
