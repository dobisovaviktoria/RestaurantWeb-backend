package be.kdg.keepdishesgoing.restaurant.domain;

import be.kdg.keepdishesgoing.restaurant.domain.exceptions.InvalidTimeRangeException;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
    private RestaurantStatus status;

    private Restaurant(UUID id, String name, Address address, String email, List<String> pictures,
                       CuisineType cuisineType, float preparationTime,
                       List<OpeningHours> openingHours, String ownerId,  RestaurantStatus status) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid contactEmail");
        if (preparationTime <= 0) throw new IllegalArgumentException("Preparation time must be positive");

        this.id = id == null ? UUID.randomUUID() : id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.pictures = List.copyOf(pictures);
        this.cuisineType = cuisineType;
        this.preparationTime = preparationTime;
        this.openingHours = List.copyOf(openingHours);
        this.ownerId = ownerId;
        this.status = status;
    }

    public static Restaurant create(String name, Address address, String email,
                                    List<String> pictures, CuisineType cuisineType,
                                    float preparationTime, List<OpeningHours> openingHours,
                                    String ownerId, RestaurantStatus status) {
        return new Restaurant(null, name, address, email, pictures, cuisineType, preparationTime, openingHours, ownerId, RestaurantStatus.AUTO);
    }

    public static Restaurant fromPersistence(
            UUID id,
            String name,
            Address address,
            String email,
            List<String> pictures,
            CuisineType cuisineType,
            float preparationTime,
            List<OpeningHours> openingHours,
            String ownerId,
            RestaurantStatus status
    ) {
        return new Restaurant(id, name, address, email, pictures, cuisineType, preparationTime, openingHours, ownerId, status);
    }

    public void updateOpeningHours(List<OpeningHours> newOpeningHours) {
        if (newOpeningHours == null || newOpeningHours.isEmpty()) {
            throw new InvalidTimeRangeException("Opening hours cannot be empty");
        }
        this.openingHours = newOpeningHours;
    }

    public void setStatus(RestaurantStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public boolean isOpenNow() {
        if (status == RestaurantStatus.OPEN) return true;
        if (status == RestaurantStatus.CLOSED) return false;
        var now = LocalTime.now();
        var day = DayOfWeek.from(java.time.LocalDate.now());
        return openingHours.stream()
                .filter(oh -> oh.dayOfWeek() == day)
                .anyMatch(oh -> !now.isBefore(oh.openingTime()) && now.isBefore(oh.closingTime()));
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Address address() {
        return address;
    }

    public String email() {
        return email;
    }

    public List<String> pictures() {
        return pictures;
    }

    public CuisineType cuisineType() {
        return cuisineType;
    }

    public float preparationTime() {
        return preparationTime;
    }

    public List<OpeningHours> openingHours() {
        return openingHours;
    }

    public String ownerId() {
        return ownerId;
    }

    public RestaurantStatus status() {return  status;}

    @Override
    public String toString() {
        return "Restaurant: \n" +
                "name = " + name + '\n' +
                "address = " + address.toString() + '\n' +
                "contactEmail= " + email + '\n' +
                "pictures = " + pictures + '\n' +
                "cuisine type = " + cuisineType + '\n' +
                "preparation time = " + preparationTime + '\n' +
                "opening hours = " + openingHours + '\n';
    }
}
