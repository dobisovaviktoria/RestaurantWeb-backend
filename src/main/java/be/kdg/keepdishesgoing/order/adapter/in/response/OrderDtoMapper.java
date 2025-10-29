package be.kdg.keepdishesgoing.order.adapter.in.response;

import be.kdg.keepdishesgoing.order.domain.Order;

public class OrderDtoMapper {
    public static OrderDto fromDomain(Order order) {
        return new OrderDto(
                order.getId(),
                order.getRestaurantId(),
                order.getStatus().name(),
                order.getTotalAmount().amount(),
                order.getStatus(),
                order.getPlacedAt()
        );
    }
}
