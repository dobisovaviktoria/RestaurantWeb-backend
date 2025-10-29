package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.MarkReadyCommand;
import be.kdg.keepdishesgoing.order.port.in.MarkReadyForPickupUseCase;
import be.kdg.keepdishesgoing.order.port.out.DeliveryMessagePort;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarkReadyForPickupUseCaseImpl implements MarkReadyForPickupUseCase {

    private final OrderEventStorePort eventStore;
    private final DeliveryMessagePort deliveryMessagePort;

    public MarkReadyForPickupUseCaseImpl(OrderEventStorePort eventStore,
                                         DeliveryMessagePort deliveryMessagePort) {
        this.eventStore = eventStore;
        this.deliveryMessagePort = deliveryMessagePort;
    }

    @Override
    public Order markReady(MarkReadyCommand command) {
        Order order = eventStore.loadEvents(command.orderId())
                .map(Order::fromEvents)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.markReadyForPickup();
        eventStore.save(order);
        deliveryMessagePort.publishOrderReady(order.getId(), order.getRestaurantId());

        return order;
    }
}
