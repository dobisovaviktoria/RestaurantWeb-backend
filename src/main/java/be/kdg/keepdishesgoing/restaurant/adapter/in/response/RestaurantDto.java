package be.kdg.keepdishesgoing.restaurant.adapter.in.response;

import be.kdg.keepdishesgoing.restaurant.domain.CuisineType;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record RestaurantDto(
        UUID id,
        String name,
        String ownerId,
        String email,
        List<String> pictures,
        CuisineType cuisineType,
        float preparationTime,
        AddressDto address,
        List<OpeningHoursDto> openingHours
) {
    public record AddressDto(
            String street,
            String number,
            String city,
            String zipcode,
            String country
    ) {}

    public record OpeningHoursDto(
            UUID id,
            DayOfWeek dayOfWeek,
            LocalTime openingTime,
            LocalTime closingTime
    ) {}
}