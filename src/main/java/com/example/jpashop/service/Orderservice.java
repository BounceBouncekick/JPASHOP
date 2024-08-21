package com.example.jpashop.service;

import com.example.jpashop.dto.DeliveryDto;
import com.example.jpashop.dto.OrderItemDto;
import com.example.jpashop.entity.*;
import com.example.jpashop.repository.DeliveryRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.ProductRepository;
import com.example.jpashop.repository.SalesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Orderservice {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final SalesRepository salesRepository;

    @Transactional
    public void order(OrderItemDto orderItemDto, String name, String uuid, DeliveryDto deliveryDto) {
        Optional<Product> optionalItem = Optional.ofNullable(productRepository.findByUuid(uuid));

        log.info("optionalItem = {}", optionalItem);
        if (optionalItem.isPresent()) {
            Product product = optionalItem.get();
            log.info("Product found: {}", product);

            // 상품을 사용하여 주문 상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(product, orderItemDto);

            // 주문 생성 (주문 상품을 배열로 넘겨줌)
            Order order = Order.createOrder(product, orderItem);
            orderRepository.save(order);

            // 배송 정보 생성 및 저장
            Delivery delivery = Delivery.createDelivery(deliveryDto);
            deliveryRepository.save(delivery);

            // 재고 차감
            product.removeStock(orderItemDto.getCount());

            Sales sales = salesRepository.findById(1L).orElse(new Sales());
            sales.addToTotalSales(order.getTotalPrice());
            salesRepository.save(sales);

        } else {
            log.warn("Product not found for UUID: {}", uuid);
            // 상품을 찾을 수 없는 경우에 대한 처리
            // 예를 들어, 예외를 던지거나 로그를 남기는 등의 작업 수행
        }
    }

    @Transactional
    public void cancelOrder(String uuid) {
        // 주문 엔티티 조회
        Order order = orderRepository.findByUuid(uuid);
        log.info("cancelOrder : {}", order);

        if (order != null) {
            double originalPrice = order.getTotalPrice(); // 원래 주문 가격을 가져옴
            log.info("취소_originalPrice :{}",originalPrice);
            // 주문 취소
            order.cancel();
            orderRepository.save(order); // 취소 상태를 저장

            // 취소된 주문 금액을 총 매출액에서 빼기
            Sales sales = salesRepository.findById(1L).orElse(new Sales());
            sales.subtractFromTotalSales(originalPrice);
            salesRepository.save(sales);
        }
    }
}