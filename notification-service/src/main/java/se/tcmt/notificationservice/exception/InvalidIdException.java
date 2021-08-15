package se.tcmt.notificationservice.exception;

public class InvalidIdException extends RuntimeException {

    public InvalidIdException(String message) {
        super(message);
    }
}