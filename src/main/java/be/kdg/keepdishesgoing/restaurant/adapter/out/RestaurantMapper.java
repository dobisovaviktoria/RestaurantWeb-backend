package be.kdg.keepdishesgoing.restaurant.adapter.out;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.AddressJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.OpeningHourJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantPicturesJpaEntity;
import be.kdg.keepdishesgoing.restaurant.domain.OpeningHours;
import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;

import java.util.List;
import java.util.UUID;

public class RestaurantMapper {

    static RestaurantJpaEntity toJpa(Restaurant restaurant) {
        return new RestaurantJpaEntity(
                restaurant.id(),
                restaurant.name(),
                restaurant.email(),
                restaurant.preparationTime(),
                restaurant.cuisineType(),
                restaurant.ownerId(),
                restaurant.status()
        );
    }

    static RestaurantPicturesJpaEntity toJpa(String url, UUID restaurantId) {
        return new RestaurantPicturesJpaEntity(
                url,
                restaurantId
        );
    }

    static AddressJpaEntity toJpa(Address address, UUID restaurantId) {
        return new AddressJpaEntity(
                UUID.randomUUID(),
                address.street(),
                address.number(),
                address.city(),
                address.zipcode(),
                address.country(),
                restaurantId
        );
    }

    static OpeningHourJpaEntity toJpa(OpeningHours oh, UUID restaurantId) {
        return new OpeningHourJpaEntity(
                oh.id(),
                oh.dayOfWeek(),
                oh.openingTime(),
                oh.closingTime(),
                restaurantId
        );
    }

    static Restaurant toDomain(
            RestaurantJpaEntity restaurantJpa,
            AddressJpaEntity addressJpa,
            List<OpeningHourJpaEntity> openingHourJpaList,
            List<RestaurantPicturesJpaEntity> pictureJpaList
    ) {
        Address address = new Address(
                addressJpa.getStreet(),
                addressJpa.getNumber(),
                addressJpa.getCity(),
                addressJpa.getZipcode(),
                addressJpa.getCountry()
        );

        List<OpeningHours> openingHours = openingHourJpaList.stream()
                .map(oh -> OpeningHours.fromPersistence(
                        oh.getId(),
                        oh.getDayOfWeek(),
                        oh.getOpeningTime(),
                        oh.getClosingTime()
                ))
                .toList();

        List<String> pictures = pictureJpaList.stream()
                .map(RestaurantPicturesJpaEntity::getUrl)
                .toList();

        return Restaurant.fromPersistence(
                restaurantJpa.getId(),
                restaurantJpa.getName(),
                address,
                restaurantJpa.getEmail(),
                pictures,
                restaurantJpa.getCuisineType(),
                restaurantJpa.getPreparationTime(),
                openingHours,
                restaurantJpa.getOwnerId(),
                restaurantJpa.getStatus()
        );
    }
}
