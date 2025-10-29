package be.kdg.keepdishesgoing.order.port.in;

import org.springframework.util.Assert;
import java.util.UUID;

public record AcceptOrderCommand(UUID orderId) {
    public AcceptOrderCommand {
        Assert.notNull(orderId, "Order ID is required");
    }
}
