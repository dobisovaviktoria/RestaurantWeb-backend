package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Owner;

import java.util.Optional;

public interface LoadOwnerPort {
    Optional<Owner> loadById(String id);
    Optional<Owner> loadByEmail(String email);
}
