package be.kdg.keepdishesgoing.order.adapter.out.repositories;

import be.kdg.keepdishesgoing.order.adapter.out.OrderSnapshotJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderSnapshotRepository extends JpaRepository<OrderSnapshotJpa, UUID> {
}
