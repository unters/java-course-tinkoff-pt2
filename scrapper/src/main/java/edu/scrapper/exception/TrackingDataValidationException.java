package edu.scrapper.exception;

public class TrackingDataValidationException extends RuntimeException {

    public TrackingDataValidationException() {
    }

    public TrackingDataValidationException(String message) {
        super(message);
    }

    public TrackingDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
