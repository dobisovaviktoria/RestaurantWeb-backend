package be.kdg.keepdishesgoing.restaurant.port.in;

import be.kdg.keepdishesgoing.restaurant.domain.OpeningHours;

import java.util.List;
import java.util.UUID;

public record UpdateOpeningHoursCommand(
        UUID restaurantId,
        List<OpeningHours> newOpeningHours
) { }
