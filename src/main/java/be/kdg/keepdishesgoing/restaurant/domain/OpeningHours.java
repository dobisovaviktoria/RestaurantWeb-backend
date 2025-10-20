package be.kdg.keepdishesgoing.restaurant.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public class OpeningHours {
    private final UUID id;
    private final DayOfWeek dayOfWeek;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    private OpeningHours(UUID id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        if (dayOfWeek == null) throw new IllegalArgumentException("DayOfWeek cannot be null");
        if (openingTime == null || closingTime == null) throw new IllegalArgumentException("Opening/closing time cannot be null");
        if (closingTime.isBefore(openingTime)) throw new IllegalArgumentException("Closing time cannot be before opening time");

        this.id = id == null ? UUID.randomUUID() : id;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static OpeningHours create(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        return new OpeningHours(null, dayOfWeek, openingTime, closingTime);
    }

    public static OpeningHours fromPersistence(UUID id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        return new OpeningHours(id, dayOfWeek, openingTime, closingTime);
    }

    public UUID id() {
        return id;
    }

    public DayOfWeek dayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime openingTime() {
        return openingTime;
    }

    public LocalTime closingTime() {
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