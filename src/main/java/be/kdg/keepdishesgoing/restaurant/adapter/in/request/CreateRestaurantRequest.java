package be.kdg.keepdishesgoing.restaurant.adapter.in.request;

import be.kdg.keepdishesgoing.restaurant.domain.CuisineType;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record CreateRestaurantRequest(
        String ownerId,
        String name,
        AddressRequest address,
        String contactEmail,
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
            LocalTime openingTime,
            LocalTime closingTime
    ) {}
}