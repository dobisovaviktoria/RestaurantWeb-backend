package be.kdg.keepdishesgoing.restaurant.adapter.in.request;

import be.kdg.keepdishesgoing.restaurant.domain.CuisineType;

import java.time.DayOfWeek;
import java.util.List;

public record CreateRestaurantRequest(
        String ownerId,
        String name,
        AddressRequest address,
        String email,
        List<String> pictures,
        CuisineType cuisineType,
        float preparationTime,
        List<OpeningHoursRequest> openingHours
) {
    public record AddressRequest(
            String street,
            String number,
            String city,
            String zipcode,
            String country
    ) {}

    public record OpeningHoursRequest(
            DayOfWeek dayOfWeek,
            String openingTime,
            String closingTime
    ) {}
}
