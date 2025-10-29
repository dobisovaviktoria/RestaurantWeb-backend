package be.kdg.keepdishesgoing.order.port.in;

import org.springframework.util.Assert;
import java.util.UUID;

public record MarkReadyCommand(UUID orderId) {
    public MarkReadyCommand {
        Assert.notNull(orderId, "Order ID is required");
    }
}
