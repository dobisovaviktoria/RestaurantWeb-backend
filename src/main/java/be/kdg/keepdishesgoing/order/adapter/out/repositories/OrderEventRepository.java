package be.kdg.keepdishesgoing.order.adapter.out.repositories;

import be.kdg.keepdishesgoing.order.adapter.out.OrderEventJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderEventRepository extends JpaRepository<OrderEventJpa, UUID> {
    List<OrderEventJpa> findByOrderIdOrderBySequenceNumberAsc(UUID aggregateId);

    @Query("SELECT MAX(e.sequenceNumber) FROM OrderEventJpa e WHERE e.orderId = :aggregateId")
    Optional<Integer> findMaxSequenceByAggregateId(UUID aggregateId);

    List<OrderEventJpa> findByOrderIdAndSequenceNumberGreaterThan(UUID orderId, int sequenceNumber);
}
