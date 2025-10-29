package be.kdg.keepdishesgoing.order.port.out;

import be.kdg.keepdishesgoing.order.domain.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderSnapshotPort {
    void saveSnapshot(Order order);
    Optional<Order> loadSnapshot(UUID orderId);
}
