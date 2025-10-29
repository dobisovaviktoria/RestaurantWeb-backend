package be.kdg.keepdishesgoing.restaurant.adapter.out;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.OwnerJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.OwnerJpaRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Owner;
import be.kdg.keepdishesgoing.restaurant.port.out.LoadOwnerPort;
import be.kdg.keepdishesgoing.restaurant.port.out.SaveOwnerPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OwnerJpaAdapter implements LoadOwnerPort, SaveOwnerPort {

    private final OwnerJpaRepository jpaRepository;

    public OwnerJpaAdapter(OwnerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Owner> loadById(String id) {
        return jpaRepository.findById(id).map(e -> Owner.fromPersistence(e.getId(), e.getEmail(), e.getName()));
    }

    @Override
    public Optional<Owner> loadByEmail(String email) {
        return jpaRepository.findByEmail(email).map(e -> Owner.fromPersistence(e.getId(), e.getEmail(), e.getName()));
    }

    @Override
    public Owner saveOwner(Owner owner) {
        OwnerJpaEntity entity = new OwnerJpaEntity(owner.id(), owner.email(), owner.name());
        jpaRepository.save(entity);
        return owner;
    }
}
