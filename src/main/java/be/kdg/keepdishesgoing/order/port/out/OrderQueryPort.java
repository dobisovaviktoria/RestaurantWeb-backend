package be.kdg.keepdishesgoing.order.port.out;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderQueryPort {
    List<OrderProjection> findPendingOrdersOlderThan(LocalDateTime dateTime);
}