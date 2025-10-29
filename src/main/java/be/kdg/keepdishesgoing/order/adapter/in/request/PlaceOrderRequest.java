package be.kdg.keepdishesgoing.order.adapter.in.request;

import be.kdg.keepdishesgoing.common.domain.Address;
import be.kdg.keepdishesgoing.order.domain.OrderLine;
import java.util.List;

public record PlaceOrderRequest(
        String customerName,
        String customerEmail,
        Address deliveryAddress,
        List<OrderLine> orderLines
) {}
