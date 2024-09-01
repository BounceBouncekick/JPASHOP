package com.example.jpashop.service;

import com.example.jpashop.dto.DeliveryDto;
import com.example.jpashop.dto.OrderItemDto;
import com.example.jpashop.entity.*;
import com.example.jpashop.postorder.DeliveryPostOrder;
import com.example.jpashop.postorder.SalesUpdatePostOrder;
import com.example.jpashop.postorder.StockReductionPostOrder;
import com.example.jpashop.postorder.PostOrder;
import com.example.jpashop.postordercancel.SubtractSales;
import com.example.jpashop.repository.DeliveryRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.ProductRepository;
import com.example.jpashop.repository.SalesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Orderservice {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final SalesRepository salesRepository;
    private final RedisTemplate<String, Object> redisTemplate;

        private final ExecutorService executorService = Executors.newFixedThreadPool(4);

        @Transactional
        public void order(OrderItemDto orderItemDto, String name, String uuid, DeliveryDto deliveryDto) {
            Product product = (Product) redisTemplate.opsForValue().get(uuid);
            if (product == null) {
                // Redis에 캐시가 없는 경우, 데이터베이스에서 조회
                product = productRepository.findByUuid(uuid);
                if (product != null) {
                    // Redis에 제품 정보를 캐싱
                    redisTemplate.opsForValue().set(uuid, product);
                }
            }

            if (product != null) {
                log.info("Product found: {}", product);

                // 상품을 사용하여 주문 상품 생성
                OrderItem orderItem = OrderItem.createOrderItem(product, orderItemDto);

                // 주문 생성 (주문 상품을 배열로 넘겨줌)
                Order order = Order.createOrder(product, orderItem);
                orderRepository.save(order);

                // product는 effectively final이므로 변경하지 않음
                final Product finalProduct = product;
                final int orderCount = orderItemDto.getCount();

                PostOrder deliveryStrategy = new DeliveryPostOrder(deliveryRepository, deliveryDto);
                executorService.submit(() -> deliveryStrategy.execute(order));

                PostOrder stockReductionStrategy = new StockReductionPostOrder(finalProduct, orderCount);
                executorService.submit(() -> stockReductionStrategy.execute(order));

                PostOrder salesUpdateStrategy = new SalesUpdatePostOrder(salesRepository);
                executorService.submit(() -> salesUpdateStrategy.execute(order));

            } else {
                log.warn("Product not found for UUID: {}", uuid);
                // 상품을 찾을 수 없는 경우에 대한 처리
            }
        }



    @Transactional
    public void cancelOrder(String uuid) {
        // 주문 엔티티 조회
        Order order = orderRepository.findByUuid(uuid);
        if (order != null) {
            double originalPrice = order.getTotalPrice(); // 원래 주문 가격을 가져옴

            order.cancel();
            orderRepository.save(order); // 취소 상태를 저장

            new SubtractSales(salesRepository).execute(originalPrice);
        }
    }
}