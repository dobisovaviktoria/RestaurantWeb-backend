package be.kdg.keepdishesgoing.order.port.in;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;

import java.util.Optional;
import java.util.UUID;

public interface TrackOrderUseCase {
    Optional<OrderProjection> trackOrder(UUID orderId);
}
