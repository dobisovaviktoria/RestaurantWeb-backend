package be.kdg.keepdishesgoing.restaurant.domain.exceptions;

public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException(String message) {
        super(message);
    }
}
