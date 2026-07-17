package com.zeldev.ride_service.controller;

import com.zeldev.ride_service.dto.RideRequest;
import com.zeldev.ride_service.dto.RideResponse;
import com.zeldev.ride_service.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
@Slf4j
public class RideController {
    private final RideService rideService;

    //when rider requests a new ride
    @PostMapping("/request")
    public ResponseEntity<RideResponse> rideRequest(@RequestBody @Valid RideRequest request) {
        log.info("Ride request received from rider {}", request.riderId());
        return ResponseEntity.status(CREATED).body(rideService.rideRequest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable("id") String rideId) {
        return ResponseEntity.status(OK).body(rideService.getById(rideId));
    }

    @GetMapping("/rider/{id}")
    public ResponseEntity<List<RideResponse>> getRidesByRider(@PathVariable("id") String riderId) {
        return ResponseEntity.status(OK).body(rideService.getRidesByRide(riderId));
    }

    @PutMapping("/{ride_id}/start")
    public ResponseEntity<RideResponse> startRide(@PathVariable("ride_id") String rideId) {
        return ResponseEntity.status(OK).body(rideService.startRide(rideId));
    }

    @PutMapping("/{ride_id}/finish")
    public ResponseEntity<RideResponse> finishRide(@PathVariable("ride_id") String rideId) {
        return ResponseEntity.status(OK).body(rideService.finishRide(rideId));
    }

    @PutMapping("/{ride_id}/cancel")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable("ride_id") String rideId) {
        return ResponseEntity.status(OK).body(rideService.cancelRide(rideId));
    }
}
