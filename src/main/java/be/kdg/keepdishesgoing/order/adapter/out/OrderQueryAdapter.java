package be.kdg.keepdishesgoing.order.adapter.out;

import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderProjectionRepository;
import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import be.kdg.keepdishesgoing.order.port.out.OrderQueryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderQueryAdapter implements OrderQueryPort {
    private final OrderProjectionRepository projectionRepo;

    public OrderQueryAdapter(OrderProjectionRepository projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    @Override
    public List<OrderProjection> findPendingOrdersOlderThan(LocalDateTime dateTime) {
        return projectionRepo.findByStatusAndPlacedAtBefore(
                OrderStatus.PENDING,
                dateTime
        );
    }
}