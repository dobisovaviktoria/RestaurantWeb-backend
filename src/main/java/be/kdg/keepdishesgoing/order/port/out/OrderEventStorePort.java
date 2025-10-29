package be.kdg.keepdishesgoing.order.port.out;

import be.kdg.keepdishesgoing.common.events.DomainEvent;
import be.kdg.keepdishesgoing.order.domain.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderEventStorePort {
    void save(Order order);
    Optional<List<DomainEvent>> loadEvents(UUID orderId);
}
