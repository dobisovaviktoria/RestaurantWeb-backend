package be.kdg.keepdishesgoing.order.core;

import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;
import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderProjectionRepository;
import be.kdg.keepdishesgoing.order.port.in.TrackOrderUseCase;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TrackOrderUseCaseImpl implements TrackOrderUseCase {

    private final OrderProjectionRepository projectionRepo;

    public TrackOrderUseCaseImpl(OrderProjectionRepository projectionRepo) {
        this.projectionRepo = projectionRepo;
    }

    @Override
    public Optional<OrderProjection> trackOrder(UUID orderId) {
        return projectionRepo.findById(orderId);
    }
}
