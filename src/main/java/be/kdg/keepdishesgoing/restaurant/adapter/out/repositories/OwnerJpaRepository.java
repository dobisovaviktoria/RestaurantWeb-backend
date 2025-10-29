package be.kdg.keepdishesgoing.restaurant.adapter.out.repositories;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.OwnerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerJpaRepository extends JpaRepository<OwnerJpaEntity,String> {
    Optional<OwnerJpaEntity> findByEmail(String email);
}
