package be.kdg.keepdishesgoing.order.adapter.in;

import be.kdg.keepdishesgoing.order.adapter.in.request.PlaceOrderRequest;
import be.kdg.keepdishesgoing.order.adapter.in.request.RejectOrderRequest;
import be.kdg.keepdishesgoing.order.adapter.in.response.OrderDto;
import be.kdg.keepdishesgoing.order.adapter.in.response.OrderDtoMapper;
import be.kdg.keepdishesgoing.order.adapter.out.OrderProjection;
import be.kdg.keepdishesgoing.order.adapter.out.repositories.OrderProjectionRepository;
import be.kdg.keepdishesgoing.order.domain.Order;
import be.kdg.keepdishesgoing.order.port.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final AcceptOrderUseCase acceptOrderUseCase;
    private final RejectOrderUseCase rejectOrderUseCase;
    private final MarkReadyForPickupUseCase markReadyUseCase;
    private final TrackOrderUseCase trackOrderUseCase;
    private final OrderProjectionRepository projectionRepo;

    public OrderController(
            PlaceOrderUseCase placeOrderUseCase,
            AcceptOrderUseCase acceptOrderUseCase,
            RejectOrderUseCase rejectOrderUseCase,
            MarkReadyForPickupUseCase markReadyUseCase,
            TrackOrderUseCase trackOrderUseCase,
            OrderProjectionRepository projectionRepo) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.acceptOrderUseCase = acceptOrderUseCase;
        this.rejectOrderUseCase = rejectOrderUseCase;
        this.markReadyUseCase = markReadyUseCase;
        this.trackOrderUseCase = trackOrderUseCase;
        this.projectionRepo = projectionRepo;
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(
            @PathVariable UUID restaurantId,
            @RequestBody PlaceOrderRequest request) {

        PlaceOrderCommand command = new PlaceOrderCommand(
                restaurantId,
                request.customerName(),
                request.customerEmail(),
                request.deliveryAddress(),
                request.orderLines()
        );

        Order order = placeOrderUseCase.placeOrder(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderDtoMapper.fromDomain(order));
    }

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId) {

        acceptOrderUseCase.acceptOrder(new AcceptOrderCommand(orderId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId,
            @RequestBody RejectOrderRequest request) {

        rejectOrderUseCase.rejectOrder(
                new RejectOrderCommand(orderId, request.reason())
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/ready")
    public ResponseEntity<Void> markReady(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId) {

        markReadyUseCase.markReady(new MarkReadyCommand(orderId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/track")
    public ResponseEntity<OrderProjection> trackOrder(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId) {

        return trackOrderUseCase.trackOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderProjection>> getRestaurantOrders(
            @PathVariable UUID restaurantId) {

        List<OrderProjection> orders = projectionRepo.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(orders);
    }
}