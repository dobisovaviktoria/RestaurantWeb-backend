package be.kdg.keepdishesgoing.order.port.in;

import java.util.UUID;

public interface MarkOrderDeliveredUseCase {
    void markDelivered(UUID orderId);
}
