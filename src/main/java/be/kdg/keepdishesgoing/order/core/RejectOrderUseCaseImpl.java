package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.RejectOrderCommand;
import be.kdg.keepdishesgoing.order.port.in.RejectOrderUseCase;
import be.kdg.keepdishesgoing.order.port.out.OrderEventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RejectOrderUseCaseImpl implements RejectOrderUseCase {

    private final OrderEventStorePort eventStore;

    public RejectOrderUseCaseImpl(OrderEventStorePort eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Order rejectOrder(RejectOrderCommand command) {
        Order order = eventStore.loadEvents(command.orderId())
                .map(Order::fromEvents)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.reject(command.reason());
        eventStore.save(order);
        return order;
    }
}
