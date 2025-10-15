package be.kdg.keepdishesgoing.restaurant.domain;

import java.util.List;
import java.util.UUID;

public class Restaurant {
    private UUID id;
    private String name;
    private Address address;
    private String email;
    private List<String> pictures;
    private CuisineType cuisineType;
    private float preparationTime;
    private List<OpeningHours> openingHours;
    private String ownerId;

    public Restaurant(String name, Address address, String email, List<String> pictures, CuisineType cuisineType, float preparationTime, List<OpeningHours> openingHours,  String ownerId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.email = email;
        this.pictures = pictures;
        this.cuisineType = cuisineType;
        this.preparationTime = preparationTime;
        this.openingHours = openingHours;
        this.ownerId = ownerId;
    }

    public Restaurant(UUID id, String name, Address address, String email, List<String> pictures, CuisineType cuisineType, float preparationTime, List<OpeningHours> openingHours, String ownerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.pictures = pictures;
        this.cuisineType = cuisineType;
        this.preparationTime = preparationTime;
        this.openingHours = openingHours;
        this.ownerId = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public CuisineType getCuisineType() {
        return cuisineType;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return "Restaurant: \n" +
                "name = " + name + '\n' +
                "address = " + address.toString() + '\n' +
                "email= " + email + '\n' +
                "pictures = " + pictures + '\n' +
                "cuisine type = " + cuisineType + '\n' +
                "preparation time = " + preparationTime + '\n' +
                "opening hours = " + openingHours + '\n';
    }
}
