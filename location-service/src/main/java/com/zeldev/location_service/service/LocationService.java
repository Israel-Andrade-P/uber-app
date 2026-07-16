package com.zeldev.location_service.service;

import com.zeldev.location_service.dto.DriverLocationRequest;
import com.zeldev.location_service.dto.NearbyDriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        log.info("Finding nearby drivers at lat: {} lgt: {} within {}km", latitude, longitude, radius);

        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radius, Metrics.KILOMETERS);

        Circle searchArea = new Circle(point, distance);

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(
                DRIVERS_GEO_KEY,
                searchArea,
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeCoordinates()
                        .includeDistance()
                        .sortAscending()
                        .limit(10)
        );

        List<NearbyDriverResponse> nearbyDrivers = new ArrayList<>();

        if (results == null) return nearbyDrivers;

        results.getContent().forEach(result -> {
            RedisGeoCommands.GeoLocation<String> location = result.getContent();
            nearbyDrivers.add(
                    NearbyDriverResponse.builder()
                            .driverId(location.getName())
                            .latitude(location.getPoint().getY())
                            .longitude(location.getPoint().getX())
                            .distanceInKm(result.getDistance().getValue())
                            .build()
            );
        });

        log.info("Drivers found nearby: {}", nearbyDrivers.size());
        return nearbyDrivers;
    }

    public void removeDriver(String driverId) {
        log.info("Removing driver {}", driverId);
        redisTemplate.opsForGeo().remove(driverId);
    }
}
