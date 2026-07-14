package com.zeldev.location_service.service;

import com.zeldev.location_service.dto.DriverLocationRequest;
import com.zeldev.location_service.dto.NearbyDriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String DRIVERS_GEO_KEY = "drivers:locations";

    public void updateLocation(DriverLocationRequest request) {
        log.info("Updating location for driver {}", request.driverId());

        //longitude FIRST, latitude SECOND
        Point driverPoint = new Point(request.longitude(), request.latitude());

        redisTemplate.opsForGeo().add(DRIVERS_GEO_KEY, driverPoint, request.driverId());

        log.info("Location updated for driver {}", request.driverId());
    }

    public List<NearbyDriverResponse> getNearbyDrivers(double latitude, double longitude, double radius) {
        return null;
    }

    public void removeDriver(String driverId) {

    }
}
