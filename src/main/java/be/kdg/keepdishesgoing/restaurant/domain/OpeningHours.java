package be.kdg.keepdishesgoing.restaurant.domain;

import java.time.DayOfWeek;
import java.util.UUID;

public class OpeningHours {
    private UUID id;
    private DayOfWeek dayOfWeek;
    private String openingTime;
    private String closingTime;

    public OpeningHours(DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        this.id = UUID.randomUUID();
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public OpeningHours(UUID id, DayOfWeek dayOfWeek, String openingTime, String closingTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public UUID getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    @Override
    public String toString() {
        return "Opening Hours: \n" +
                "Day: " + dayOfWeek + '\n' +
                "   open: " + openingTime + '\n' +
                "   close: " + closingTime + '\n';
    }
}
