package com.zeldev.ride_service.exception;

public class RideNotAcceptedException extends RuntimeException {
    public RideNotAcceptedException(String message) {
        super(message);
    }
}
