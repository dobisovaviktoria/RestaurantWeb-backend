package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.RestaurantStatus;

import java.util.UUID;

public record ChangeRestaurantStatusCommand(
        UUID restaurantId,
        RestaurantStatus status
) { }
