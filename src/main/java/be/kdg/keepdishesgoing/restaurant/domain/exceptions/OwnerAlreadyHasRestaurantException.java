package be.kdg.keepdishesgoing.restaurant.domain.exceptions;

public class OwnerAlreadyHasRestaurantException extends Exception {
    public OwnerAlreadyHasRestaurantException(String message) {
        super(message);
    }
}