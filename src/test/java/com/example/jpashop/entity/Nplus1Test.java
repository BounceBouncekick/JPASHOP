package com.example.jpashop.entity;

import com.example.jpashop.config.SecurityConfig;
import com.example.jpashop.jwt.JWTUtil;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)  // 테스트용 보안 설정 적용
public class Nplus1Test {

    @MockBean
    private JWTUtil jwtUtil; // JWTUtil을 Mocking

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testBatchSizeAndSubselectFetch() {
        // Given
        int numberOfOrders = 1000;   // 주문의 수
        int itemsPerOrder = 2;       // 각 주문에 포함될 주문 항목의 수

        for (int i = 0; i < numberOfOrders; i++) {
            Order order = new Order();

            for (int j = 0; j < itemsPerOrder; j++) {
                Product product = Product.builder()
                        .name("Product " + j)
                        .price(1000 + j * 100)
                        .stockQuantity(100)
                        .boardwriter("Writer " + j)
                        .productname("ProductName " + j)
                        .build();

                productRepository.save(product);

                OrderItem item = OrderItem.builder()
                        .orderPrice(product.getPrice())
                        .count(j + 1)
                        .product(product)
                        .build();

                order.addOrderItem(item); // 주문에 주문 항목 추가
            }

            orderRepository.save(order); // Order와 연관된 OrderItem을 함께 저장
        }

        entityManager.clear(); // 1차 캐시 초기화 (DB로부터 데이터를 다시 가져오도록 강제)

        // When
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findAll();  // 모든 주문 조회

        // Accessing orderItems to ensure they are loaded
        orders.forEach(order -> order.getOrderItems().size());
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(orders.size()).isEqualTo(numberOfOrders); // 주문의 수가 예상과 일치하는지 검증

        // 결과를 출력하여 분석
        System.out.println("Number of Orders: " + numberOfOrders);
        System.out.println("Items per Order: " + itemsPerOrder);
        System.out.println("Total Execution Time: " + (endTime - startTime) + "ms");
    }
}
