package be.kdg.keepdishesgoing.order.adapter.out.repositories;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;
import be.kdg.keepdishesgoing.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderProjectionRepository extends JpaRepository<OrderProjection, UUID> {
    List<OrderProjection> findByRestaurantId(UUID restaurantId);
    List<OrderProjection> findByStatus(OrderStatus status);
    List<OrderProjection> findByStatusAndPlacedAtBefore(OrderStatus status, LocalDateTime before);
}