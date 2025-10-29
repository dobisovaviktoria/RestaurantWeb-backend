package be.kdg.keepdishesgoing.order.port.in;

import java.util.UUID;

public interface MarkOrderPickedUpUseCase {
    void markPickedUp(UUID orderId);
}