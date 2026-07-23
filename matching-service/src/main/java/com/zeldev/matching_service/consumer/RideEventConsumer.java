package com.zeldev.matching_service.consumer;

import com.zeldev.matching_service.event.RideRequestedEvent;
import com.zeldev.matching_service.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideEventConsumer {
    private final MatchingService matchingService;

    /*
    listens to ride.requested kafka topic
    triggers everytime ride service publishes a new ride request
     */
    @KafkaListener(topics = "ride.requested", groupId = "matching-service-group")
    public void consumeRideRequestedEvent(RideRequestedEvent event) {
        try {
            matchingService.matchDriverToRide(event);
        }catch (Exception e) {
            log.error("Error processing ride request, failed consuming event: {} - {}", event.rideId(), e.getMessage());
            //send to dead letter queue for a retry
        }
    }
}
