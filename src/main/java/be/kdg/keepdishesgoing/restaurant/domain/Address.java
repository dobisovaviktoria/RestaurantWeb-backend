package be.kdg.keepdishesgoing.restaurant.domain;

public record Address(
        String street,
        String number,
        String city,
        String zipcode,
        String country
) {
}
