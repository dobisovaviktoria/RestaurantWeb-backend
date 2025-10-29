package be.kdg.keepdishesgoing.restaurant.port.out;

import be.kdg.keepdishesgoing.restaurant.domain.Owner;

public interface SaveOwnerPort {
    Owner saveOwner(Owner owner);
}
