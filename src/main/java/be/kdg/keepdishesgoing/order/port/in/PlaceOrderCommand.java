package be.kdg.keepdishesgoing.order.port.in;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.order.domain.OrderLine;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

public record PlaceOrderCommand(
        UUID restaurantId,
        String customerName,
        String customerEmail,
        Address deliveryAddress,
        List<OrderLine> orderLines
) {
    public PlaceOrderCommand {
        Assert.notNull(restaurantId, "Restaurant ID is required");
        Assert.hasText(customerName, "Customer name is required");
        Assert.hasText(customerEmail, "Customer contactEmail is required");
        Assert.notNull(deliveryAddress, "Delivery address is required");
        Assert.notEmpty(orderLines, "Order must contain at least one item");
    }
}
