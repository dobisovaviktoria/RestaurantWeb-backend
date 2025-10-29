package be.kdg.keepdishesgoing.order.adapter.in.response;

import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDto(
        UUID orderId,
        UUID restaurantId,
        String customerName,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime placedAt
) {}
