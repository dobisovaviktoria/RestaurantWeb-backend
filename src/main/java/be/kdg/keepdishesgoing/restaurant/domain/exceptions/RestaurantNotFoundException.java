package be.kdg.keepdishesgoing.restaurant.domain.exceptions;

public class RestaurantNotFoundException extends Exception {
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
