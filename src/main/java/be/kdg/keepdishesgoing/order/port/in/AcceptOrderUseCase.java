package be.kdg.keepdishesgoing.order.port.in;

import be.kdg.keepdishesgoing.order.domain.Order;

public interface AcceptOrderUseCase {
    Order acceptOrder(AcceptOrderCommand command);
}
