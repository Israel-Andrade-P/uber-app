package com.zeldev.ride_service.consumer;

import com.zeldev.ride_service.event.RideMatchedEvent;
import com.zeldev.ride_service.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideMatchedConsumer {
    private final RideService rideService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ride.matched", groupId = "ride-service-group")
    public void consumeRideMatchedEvent(String payload) {
        try {
            RideMatchedEvent event = objectMapper.readValue(payload, RideMatchedEvent.class);

            rideService.updateRideWithDriver(event.rideId(), event.driverId());
        } catch (Exception e) {
            log.error("Error processing ride matched event, failed consuming event: {} - {}", payload, e.getMessage());
        }
    }
}
