package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.PlaceOrderCommand;
import be.kdg.keepdishesgoing.order.port.in.PlaceOrderUseCase;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaceOrderUseCaseImpl implements PlaceOrderUseCase {

    private final OrderEventStorePort eventStore;

    public PlaceOrderUseCaseImpl(OrderEventStorePort eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Order placeOrder(PlaceOrderCommand command) {
        Order order = Order.placeOrder(
                command.restaurantId(),
                command.customerName(),
                command.customerEmail(),
                command.deliveryAddress(),
                command.orderLines()
        );

        eventStore.save(order);

        return order;
    }
}
