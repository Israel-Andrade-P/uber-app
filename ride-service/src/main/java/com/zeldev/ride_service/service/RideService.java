package com.zeldev.ride_service.service;

import com.zeldev.ride_service.dto.RideRequest;
import com.zeldev.ride_service.dto.RideResponse;
import com.zeldev.ride_service.event.RideRequestedEvent;
import com.zeldev.ride_service.exception.RideNotAcceptedException;
import com.zeldev.ride_service.exception.RideNotFoundException;
import com.zeldev.ride_service.exception.RideNotStartedException;
import com.zeldev.ride_service.model.Ride;
import com.zeldev.ride_service.repository.RideRepository;
import com.zeldev.ride_service.utils.RideUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.zeldev.ride_service.enumeration.RideStatus.*;
import static com.zeldev.ride_service.utils.EventUtils.createRideRequestedEvent;
import static com.zeldev.ride_service.utils.RideUtils.toRide;
import static com.zeldev.ride_service.utils.RideUtils.toRideResponse;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
    private final RideRepository rideRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String RIDE_REQUESTED_TOPIC = "ride.requested";

    @Transactional
    public RideResponse rideRequest(RideRequest request) {
        log.info("New ride requested from rider {}", request.riderId());
        //persist ride to db
        Ride savedRide = rideRepository.save(toRide(request));

        //publish a kafka event for matching service to consume it and find nearest driver
        RideRequestedEvent event = createRideRequestedEvent(savedRide);

        //Manual serialization cause kafka serializer is broken
        String json = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(RIDE_REQUESTED_TOPIC, savedRide.getId(), json);
        log.info("RideRequestedEvent published for ride {}", savedRide.getId());

        savedRide.setStatus(MATCHING);

        return toRideResponse(savedRide);
    }

    public RideResponse getById(String rideId) {
        return toRideResponse(getRideById(rideId));
    }

    public List<RideResponse> getRidesByRider(String riderId) {
        return rideRepository.findRidesByRiderId(riderId).stream().map(RideUtils::toRideResponse).toList();
    }

    @Transactional
    public RideResponse startRide(String rideId) {
        var ride = getRideById(rideId);
        var status = ride.getStatus();

        if (status != ACCEPTED) throw new RideNotAcceptedException(String.format("Ride couldn't be started, current status: %s", status));

        ride.setStatus(RIDE_STARTED);
        ride.setRideStartedAt(now());

        return toRideResponse(ride);
    }

    @Transactional
    public RideResponse finishRide(String rideId) {
        var ride = getRideById(rideId);
        var status = ride.getStatus();

        if (status != RIDE_STARTED) throw new RideNotStartedException(String.format("Ride couldn't be finished, current status: %s", status));

        ride.setStatus(COMPLETED);
        ride.setRideFinishedAt(now());
        ride.setActualFare(ride.getEstimatedFare());

        return toRideResponse(ride);
    }

    public RideResponse cancelRide(String rideId) {
        var ride = getRideById(rideId);

        ride.setStatus(CANCELLED);

        return toRideResponse(ride);
    }

    @Transactional
    public void updateRideWithDriver(String rideId, String driverId) {
        var ride = getRideById(rideId);
        ride.setDriverId(driverId);
        ride.setStatus(ACCEPTED);
    }

    private Ride getRideById(String rideId) {
        return rideRepository.findById(rideId).orElseThrow(() -> new RideNotFoundException(String.format("Ride with id %s not found", rideId)));
    }
}
