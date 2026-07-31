package com.zeldev.matching_service.service;

import com.zeldev.matching_service.client.LocationServiceClient;
import com.zeldev.matching_service.dto.NearbyDriverResponse;
import com.zeldev.matching_service.event.RideMatchedEvent;
import com.zeldev.matching_service.event.RideRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.zeldev.matching_service.utils.EventUtils.createRideMatchedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final LocationServiceClient locationServiceClient;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String RIDE_MATCHED_TOPIC = "ride.matched";
    private static final double DEFAULT_SEARCH_RADIUS_KM = 50.0;

    public void matchDriverToRide(RideRequestedEvent event) {
        List<NearbyDriverResponse> drivers = locationServiceClient.getNearbyDrivers(event.pickupLatitude(), event.pickupLongitude(), DEFAULT_SEARCH_RADIUS_KM);

        if (drivers.isEmpty()) {
            log.warn("No drivers found close to client");
            return;
        }

        Optional<NearbyDriverResponse> closestDriver = findClosestDriver(drivers);

        if (closestDriver.isEmpty()) {
            log.warn("Couldn't find the closest driver");
            return;
        }

        NearbyDriverResponse assignedDriver = closestDriver.get();

        RideMatchedEvent matchedEvent = createRideMatchedEvent(event, assignedDriver);

        String json = objectMapper.writeValueAsString(matchedEvent);

        kafkaTemplate.send(RIDE_MATCHED_TOPIC, event.rideId(), json);
        log.info("RideMatchedEvent published for rider {} with driver {}", matchedEvent.riderId(), matchedEvent.driverId());
    }

    /*
    Driver Scoring Algorithm
    distance: 70%
    rating: 30%

    score = (1 / distance) * distanceWeight + rating * ratingWeight
     */
    private Optional<NearbyDriverResponse> findClosestDriver(List<NearbyDriverResponse> drivers) {
        double distanceWeight = 0.7;
        double ratingWeight = 0.3;

        return drivers.stream().max(Comparator.comparingDouble(driver -> {
            //the closest the driver the higher the score
            //put 0.1 to avoid division by ZERO
            double distanceScore = 1.0 / (driver.distanceInKm() + 0.1);

            //rating simulation between 4.0 and 5.0
            //in production we would get it from driver service
            double simulatedRating = 4.0 + Math.random();

            //final score
            return (distanceScore * distanceWeight) + (simulatedRating * ratingWeight);
        }));
    }
}
