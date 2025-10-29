package be.kdg.keepdishesgoing.order.port.in;

import org.springframework.util.Assert;
import java.util.UUID;

public record RejectOrderCommand(UUID orderId, String reason) {
    public RejectOrderCommand {
        Assert.notNull(orderId, "Order ID is required");
        Assert.hasText(reason, "Rejection reason is required");
    }
}
