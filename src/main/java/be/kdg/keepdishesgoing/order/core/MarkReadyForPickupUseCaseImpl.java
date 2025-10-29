package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.MarkReadyCommand;
import be.kdg.keepdishesgoing.order.port.in.MarkReadyForPickupUseCase;
import be.kdg.keepdishesgoing.order.port.out.DeliveryMessagePort;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import be.kdg.keepdishesgoing.order.port.out.OrderSnapshotPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarkReadyForPickupUseCaseImpl implements MarkReadyForPickupUseCase {
    private final OrderEventStorePort eventStore;
    private final DeliveryMessagePort deliveryMessagePort;
    private final OrderSnapshotPort snapshotPort;

    public MarkReadyForPickupUseCaseImpl(
            OrderEventStorePort eventStore,
            DeliveryMessagePort deliveryMessagePort,
            OrderSnapshotPort snapshotPort) {
        this.eventStore = eventStore;
        this.deliveryMessagePort = deliveryMessagePort;
        this.snapshotPort = snapshotPort;
    }

    @Override
    public Order markReady(MarkReadyCommand command) {
        Order order = snapshotPort.loadSnapshot(command.orderId())
                .map(snapshot -> {
                    return eventStore.loadEvents(command.orderId())
                            .map(events -> {
                                events.forEach(snapshot::applyEvent);
                                return snapshot;
                            })
                            .orElse(snapshot);
                })
                .or(() -> eventStore.loadEvents(command.orderId())
                        .map(Order::fromEvents))
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.markReadyForPickup();
        eventStore.save(order);
        deliveryMessagePort.publishOrderReady(order.getId(), order.getRestaurantId());

        return order;
    }
}
