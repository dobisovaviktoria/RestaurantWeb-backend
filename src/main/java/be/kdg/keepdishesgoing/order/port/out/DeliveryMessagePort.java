package be.kdg.keepdishesgoing.order.port.out;

import java.util.UUID;

public interface DeliveryMessagePort {
    void publishOrderAccepted(UUID orderId, UUID restaurantId);
    void publishOrderReady(UUID orderId, UUID restaurantId);
}
