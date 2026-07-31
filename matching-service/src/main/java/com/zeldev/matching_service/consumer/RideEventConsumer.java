package com.zeldev.matching_service.consumer;

import com.zeldev.matching_service.event.RideRequestedEvent;
import com.zeldev.matching_service.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideEventConsumer {
    private final MatchingService matchingService;
    private final ObjectMapper objectMapper;

    /*
    listens to ride.requested kafka topic
    triggers everytime ride service publishes a new ride request
     */
    @KafkaListener(topics = "ride.requested", groupId = "matching-service-group")
    public void consumeRideRequestedEvent(String payload) {
        try {
            RideRequestedEvent event = objectMapper.readValue(payload, RideRequestedEvent.class);
            matchingService.matchDriverToRide(event);
        }catch (Exception e) {
            log.error("Error processing ride request, failed consuming event: {} - {}", payload, e.getMessage());
            //send to dead letter queue for a retry
        }
    }
}
