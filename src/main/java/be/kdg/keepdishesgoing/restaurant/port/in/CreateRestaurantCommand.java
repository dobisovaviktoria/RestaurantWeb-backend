package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.Address;
import be.kdg.keepdishesgoing.restaurant.domain.CuisineType;
import be.kdg.keepdishesgoing.restaurant.domain.OpeningHours;
import org.springframework.util.Assert;

import java.util.List;

public record CreateRestaurantCommand(
        String ownerId,
        String name,
        Address address,
        String email,
        List<String> pictures,
        CuisineType cuisineType,
        float preparationTime,
        List<OpeningHours> openingHours
) {
    public CreateRestaurantCommand {
        Assert.hasText(name, "Restaurant name is required");
        Assert.hasText(email, "Contact email is required");
        Assert.notEmpty(pictures, "At least one picture URL is required");
        Assert.isTrue(preparationTime > 0, "Preparation time must be positive");
    }
}
