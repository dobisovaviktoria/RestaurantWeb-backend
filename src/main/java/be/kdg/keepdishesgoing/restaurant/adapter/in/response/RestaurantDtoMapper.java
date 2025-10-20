package be.kdg.keepdishesgoing.restaurant.adapter.in.response;

import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import java.util.stream.Collectors;

public class RestaurantDtoMapper {
    public static RestaurantDto fromDomain(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.id(),
                restaurant.name(),
                restaurant.ownerId(),
                restaurant.email(),
                restaurant.pictures(),
                restaurant.cuisineType(),
                restaurant.preparationTime(),
                new RestaurantDto.AddressDto(
                        restaurant.address().street(),
                        restaurant.address().number(),
                        restaurant.address().city(),
                        restaurant.address().zipcode(),
                        restaurant.address().country()
                ),
                restaurant.openingHours().stream()
                        .map(oh -> new RestaurantDto.OpeningHoursDto(
                                oh.id(),
                                oh.dayOfWeek(),
                                oh.openingTime(),  // use domain accessor
                                oh.closingTime()   // use domain accessor
                        ))
                        .collect(Collectors.toList())
        );
    }
}
