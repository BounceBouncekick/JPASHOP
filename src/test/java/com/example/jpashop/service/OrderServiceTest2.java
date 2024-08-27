package com.example.jpashop.service;

import com.example.jpashop.config.CacheConfig;
import com.example.jpashop.config.RedisConfig;
import com.example.jpashop.config.SecurityConfig;
import com.example.jpashop.dto.DeliveryDto;
import com.example.jpashop.dto.OrderItemDto;
import com.example.jpashop.entity.Order;
import com.example.jpashop.entity.Product;
import com.example.jpashop.jwt.JWTUtil;
import com.example.jpashop.repository.DeliveryRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.ProductRepository;
import com.example.jpashop.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class, RedisConfig.class, CacheConfig.class})// 테스트용 보안 설정 적용
public class OrderServiceTest2 {

    @MockBean
    private JWTUtil jwtUtil; // JWTUtil을 Mocking

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Orderservice orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    public void setUp() {
        // Redis에 있는 기존 데이터를 모두 삭제하여 초기화합니다.
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @Transactional
    public void testOrderSuccessWithLargeNumberOfOrders() {
        // Given
        int numberOfProducts = 100;  // 생성할 상품 개수
        int numberOfOrders = 10000;  // 생성할 주문 개수

        List<Product> products = new ArrayList<>();

        // 100개의 서로 다른 상품 생성 및 저장
        for (int i = 0; i < numberOfProducts; i++) {
            String productName = "Sample Product " + i;
            String boardwriter = "Test Writer " + i;

            Product product = Product.builder()
                    .name(productName)
                    .price(1000 + i * 10)  // 예: 가격을 다르게 설정
                    .stockQuantity(1000)  // 각 상품에 대해 충분한 재고 설정
                    .boardwriter(boardwriter)
                    .productname("Product Name " + i)
                    .build();
            productRepository.save(product);
            products.add(product);

            // Redis에 상품 캐시 저장
            redisTemplate.opsForValue().set(product.getUuid(), product);
        }

        // OrderItemDto 및 DeliveryDto 설정
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfOrders; i++) {
            // 랜덤으로 상품 선택
            Product product = products.get(i % numberOfProducts);

            OrderItemDto orderItemDto = OrderItemDto.builder()
                    .count(1)  // 각 주문 항목은 1개로 설정
                    .orderPrice(product.getPrice())  // 제품 가격과 일치
                    .build();

            // 랜덤한 배송 주소 생성
            String city = "Sample City " + (i % numberOfProducts);
            String street = "Sample Street " + i;
            String zipcode = String.format("%05d", 12345 + i);

            DeliveryDto deliveryDto = DeliveryDto.builder()
                    .city(city)
                    .street(street)
                    .zipcode(zipcode)
                    .build();

            // 주문 생성
            orderService.order(orderItemDto, product.getName(), product.getUuid(), deliveryDto);
        }

        long endTime = System.currentTimeMillis();

        // Then
        List<Order> savedOrders = orderRepository.findAll();
        assertThat(savedOrders.size()).isEqualTo(numberOfOrders);  // 주문이 numberOfOrders만큼 생성되었는지 확인

        // DB 저장 시간 출력
        System.out.println(numberOfOrders + "개의 주문을 저장하는 데 걸린 시간: " + (endTime - startTime) + "ms");
    }
}


