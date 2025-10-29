package be.kdg.keepdishesgoing.restaurant.adapter.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "address", schema = "kdg_restaurant")
public class AddressJpaEntity {
    @Id
    private UUID id;

    private String street;
    private String number;
    private String city;
    private String zipcode;
    private String country;
    @Column(name = "restaurant_id", nullable = false, unique = true)
    private UUID restaurantId;

    public AddressJpaEntity(UUID id, String street, String number, String city, String zipcode, String country, UUID restaurantId) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.restaurantId = restaurantId;
    }

    public AddressJpaEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
