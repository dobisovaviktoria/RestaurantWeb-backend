package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.RejectOrderCommand;
import be.kdg.keepdishesgoing.order.port.in.RejectOrderUseCase;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import be.kdg.keepdishesgoing.order.port.out.OrderSnapshotPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RejectOrderUseCaseImpl implements RejectOrderUseCase {
    private final OrderEventStorePort eventStore;
    private final OrderSnapshotPort snapshotPort;

    public RejectOrderUseCaseImpl(
            OrderEventStorePort eventStore,
            OrderSnapshotPort snapshotPort) {
        this.eventStore = eventStore;
        this.snapshotPort = snapshotPort;
    }

    @Override
    public Order rejectOrder(RejectOrderCommand command) {
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

        order.reject(command.reason());
        eventStore.save(order);
        return order;
    }
}
