package be.kdg.keepdishesgoing.common.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime eventPit();
}
