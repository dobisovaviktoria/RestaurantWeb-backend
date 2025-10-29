package be.kdg.keepdishesgoing.restaurant.adapter.out;

import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.AddressJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.OpeningHourJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.entities.RestaurantPicturesJpaEntity;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.AddressJpaRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.OpeningHoursRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.RestaurantJpaRepository;
import be.kdg.keepdishesgoing.restaurant.adapter.out.repositories.RestaurantPicturesRepository;
import be.kdg.keepdishesgoing.restaurant.domain.Restaurant;
import be.kdg.keepdishesgoing.restaurant.port.out.CreateRestaurantPort;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantLoadPort;
import be.kdg.keepdishesgoing.restaurant.port.out.RestaurantUpdatePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RestaurantJpaAdapter implements CreateRestaurantPort, RestaurantLoadPort, RestaurantUpdatePort {
    private final RestaurantJpaRepository restaurantRepository;
    private final AddressJpaRepository addressRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final RestaurantPicturesRepository restaurantPicturesRepository;

    public RestaurantJpaAdapter(RestaurantJpaRepository restaurantRepository,
                                AddressJpaRepository addressRepository,
                                OpeningHoursRepository openingHoursRepository,
                                RestaurantPicturesRepository restaurantPicturesRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.restaurantPicturesRepository = restaurantPicturesRepository;
    }

    @Override
    @Transactional
    public Restaurant create(Restaurant restaurant) {
        RestaurantJpaEntity restaurantEntity = RestaurantMapper.toJpa(restaurant);
        restaurantRepository.save(restaurantEntity);

        AddressJpaEntity addressEntity = RestaurantMapper.toJpa(restaurant.address(), restaurant.id());
        addressRepository.save(addressEntity);

        restaurant.openingHours().forEach(oh -> {
            OpeningHourJpaEntity ohEntity = RestaurantMapper.toJpa(oh, restaurant.id());
            openingHoursRepository.save(ohEntity);
        });

        restaurant.pictures().forEach(p -> {
            RestaurantPicturesJpaEntity rpEntity = RestaurantMapper.toJpa(p, restaurant.id());
            restaurantPicturesRepository.save(rpEntity);
        });

        return restaurant;
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return restaurantRepository.findById(id)
                .map(jpa -> {
                    Optional<AddressJpaEntity> addressOptional = addressRepository.findByRestaurantId(id);
                    List<OpeningHourJpaEntity> openingHourJpaList = openingHoursRepository.findAllByRestaurantId(id);
                    List<RestaurantPicturesJpaEntity> picturesJpaList = restaurantPicturesRepository.findAllByRestaurantId(id);
                    AddressJpaEntity addressJpa;
                    if (!addressOptional.isPresent()) {
                        addressJpa = null;
                    }
                    else {
                        addressJpa =  addressOptional.get();
                    }
                    return RestaurantMapper.toDomain(jpa, addressJpa, openingHourJpaList, picturesJpaList);
                });
    }

    @Override
    public List<Restaurant> findAll() {
        List<RestaurantJpaEntity> allRestaurants = restaurantRepository.findAll();

        return allRestaurants.stream().map(jpa -> {
            UUID restaurantId = jpa.getId();

            Optional<AddressJpaEntity> addressOptional = addressRepository.findByRestaurantId(restaurantId);
            List<OpeningHourJpaEntity> openingHourJpaList = openingHoursRepository.findAllByRestaurantId(restaurantId);
            List<RestaurantPicturesJpaEntity> picturesJpaList = restaurantPicturesRepository.findAllByRestaurantId(restaurantId);

            AddressJpaEntity addressJpa = addressOptional.orElse(null);

            return RestaurantMapper.toDomain(jpa, addressJpa, openingHourJpaList, picturesJpaList);
        }).toList();
    }

    @Override
    public Optional<Restaurant> findByOwnerId(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .map(jpa -> {
                    UUID restaurantId = jpa.getId();

                    Optional<AddressJpaEntity> addressOptional = addressRepository.findByRestaurantId(restaurantId);
                    List<OpeningHourJpaEntity> openingHourJpaList = openingHoursRepository.findAllByRestaurantId(restaurantId);
                    List<RestaurantPicturesJpaEntity> picturesJpaList = restaurantPicturesRepository.findAllByRestaurantId(restaurantId);

                    AddressJpaEntity addressJpa = addressOptional.orElse(null);

                    return RestaurantMapper.toDomain(jpa, addressJpa, openingHourJpaList, picturesJpaList);
                });
    }

    @Override
    public void save(Restaurant restaurant) {
        RestaurantJpaEntity restaurantEntity = RestaurantMapper.toJpa(restaurant);
        restaurantRepository.save(restaurantEntity);

        UUID restaurantId = restaurantEntity.getId();

        Optional<AddressJpaEntity> existingAddress = addressRepository.findByRestaurantId(restaurantId);
        AddressJpaEntity addressEntity;
        if (existingAddress.isPresent()) {
            addressEntity = existingAddress.get();
            addressEntity.setStreet(restaurant.address().street());
            addressEntity.setNumber(restaurant.address().number());
            addressEntity.setCity(restaurant.address().city());
            addressEntity.setZipcode(restaurant.address().zipcode());
            addressEntity.setCountry(restaurant.address().country());
        } else {
            addressEntity = RestaurantMapper.toJpa(restaurant.address(), restaurantId);
        }
        addressRepository.save(addressEntity);

        restaurant.pictures().forEach(pic ->
                restaurantPicturesRepository.save(RestaurantMapper.toJpa(pic, restaurantId))
        );

        restaurant.openingHours().forEach(oh ->
                openingHoursRepository.save(RestaurantMapper.toJpa(oh, restaurantId))
        );
    }
}

