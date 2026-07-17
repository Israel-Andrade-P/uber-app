package com.zeldev.ride_service.service;

import com.zeldev.ride_service.dto.RideRequest;
import com.zeldev.ride_service.dto.RideResponse;
import com.zeldev.ride_service.event.RideRequestedEvent;
import com.zeldev.ride_service.model.Ride;
import com.zeldev.ride_service.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zeldev.ride_service.enumeration.RideStatus.MATCHING;
import static com.zeldev.ride_service.utils.EventUtils.createRideRequestedEvent;
import static com.zeldev.ride_service.utils.RideUtils.toRide;
import static com.zeldev.ride_service.utils.RideUtils.toRideResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
    private final RideRepository rideRepository;
    private final KafkaTemplate<String, RideRequestedEvent> kafkaTemplate;

    private static final String RIDE_REQUESTED_TOPIC = "ride.requested";

    @Transactional
    public RideResponse rideRequest(RideRequest request) {
        log.info("New ride requested from rider {}", request.riderId());
        //persist ride to db
        Ride savedRide = rideRepository.save(toRide(request));

        //publish a kafka event for matching service to consume it and find nearest driver
        RideRequestedEvent event = createRideRequestedEvent(savedRide);

        kafkaTemplate.send(RIDE_REQUESTED_TOPIC, savedRide.getId(), event);
        log.info("RideRequestedEvent published for ride {}", savedRide.getId());

        savedRide.setStatus(MATCHING);

        return toRideResponse(savedRide);
    }

    public RideResponse getById(String rideId) {
        return null;
    }

    public List<RideResponse> getRidesByRide(String riderId) {
        return null;
    }

    public RideResponse startRide(String rideId) {
        return null;
    }

    public RideResponse finishRide(String rideId) {
        return null;
    }

    public RideResponse cancelRide(String rideId) {
        return null;
    }
}
