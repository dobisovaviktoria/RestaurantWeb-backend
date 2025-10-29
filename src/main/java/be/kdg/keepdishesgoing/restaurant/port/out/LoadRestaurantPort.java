package be.kdg.keepdishesgoing.restaurant.port.out;

public interface LoadRestaurantPort {
    boolean existsByOwnerId(String ownerId);
}
