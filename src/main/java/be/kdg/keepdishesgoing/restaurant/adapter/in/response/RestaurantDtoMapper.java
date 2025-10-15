package be.kdg.keepdishesgoing.restaurant.adapter.in.response;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;

public class RestaurantDtoMapper {
    public static RestaurantDto fromDomain(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getOwnerId(),
                restaurant.getEmail(),
                restaurant.getPictures(),
                restaurant.getCuisineType(),
                restaurant.getPreparationTime(),
                new RestaurantDto.AddressDto(
                        restaurant.getAddress().street(),
                        restaurant.getAddress().number(),
                        restaurant.getAddress().city(),
                        restaurant.getAddress().zipcode(),
                        restaurant.getAddress().country()
                ),
                restaurant.getOpeningHours().stream()
                        .map(oh -> new RestaurantDto.OpeningHoursDto(
                                oh.getId(),
                                oh.getDayOfWeek(),
                                oh.getOpeningTime(),
                                oh.getClosingTime()
                        ))
                        .toList()
        );
    }
}
