package com.zeldev.ride_service.repository;

import com.zeldev.ride_service.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, String> {

    @Query("SELECT r FROM Ride r WHERE r.riderId = ?1 ORDER BY r.createdAt DESC")
    List<Ride> findRidesByRiderId(String riderId);
}
