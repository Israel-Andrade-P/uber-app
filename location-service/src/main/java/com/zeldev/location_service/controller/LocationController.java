package com.zeldev.location_service.controller;

import com.zeldev.location_service.dto.DriverLocationRequest;
import com.zeldev.location_service.dto.NearbyDriverResponse;
import com.zeldev.location_service.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final LocationService locationService;

    //driver hitting this endpoint every 3 secs updating their location
    @PostMapping("/drivers/update")
    public ResponseEntity<String> updateDriverLocation(@RequestBody DriverLocationRequest request) {
        locationService.updateLocation(request);
        return ResponseEntity.status(OK).body("Driver's location updated");
    }

    //matching service calls it whenever a ride is requested
    @GetMapping("/drivers/nearby")
    public ResponseEntity<List<NearbyDriverResponse>> getNearbyDrivers(@RequestParam double latitude,
                                                                       @RequestParam double longitude,
                                                                       @RequestParam(defaultValue = "5.0") double radius) {
        return ResponseEntity.status(OK).body(locationService.getNearbyDrivers(latitude, longitude, radius));
    }

    //gets called whenever a driver goes offline
    @DeleteMapping("/drivers/{driverId}")
    public ResponseEntity<String> removeDriver(@PathVariable String driverId) {
        locationService.removeDriver(driverId);
        return ResponseEntity.status(OK).body("Driver removed");
    }
}
