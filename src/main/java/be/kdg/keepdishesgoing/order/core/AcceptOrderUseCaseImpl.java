package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.AcceptOrderCommand;
import be.kdg.keepdishesgoing.order.port.in.AcceptOrderUseCase;
import be.kdg.keepdishesgoing.order.port.out.DeliveryMessagePort;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AcceptOrderUseCaseImpl implements AcceptOrderUseCase {

    private final OrderEventStorePort eventStore;
    private final DeliveryMessagePort deliveryMessagePort;

    public AcceptOrderUseCaseImpl(OrderEventStorePort eventStore,
                                  DeliveryMessagePort deliveryMessagePort) {
        this.eventStore = eventStore;
        this.deliveryMessagePort = deliveryMessagePort;
    }

    @Override
    public Order acceptOrder(AcceptOrderCommand command) {
        Order order = eventStore.loadEvents(command.orderId())
                .map(Order::fromEvents)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.accept();
        eventStore.save(order);
        deliveryMessagePort.publishOrderAccepted(order.getId(), order.getRestaurantId());

        return order;
    }
}
