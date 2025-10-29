package be.kdg.keepdishesgoing.common.domain;

public record Address(
        String street,
        String number,
        String city,
        String zipcode,
        String country
) {
}
