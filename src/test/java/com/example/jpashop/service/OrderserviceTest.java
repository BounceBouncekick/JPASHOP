package com.example.jpashop.service;


import com.example.jpashop.dto.DeliveryDto;
import com.example.jpashop.dto.OrderItemDto;
import com.example.jpashop.entity.Delivery;
import com.example.jpashop.entity.Order;
import com.example.jpashop.entity.Product;
import com.example.jpashop.repository.DeliveryRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class OrderserviceTest {

    @Autowired
    private Orderservice orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Product product;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


    @Test
    @Transactional
    @Rollback(false)
    public void Product(){
        product = Product.builder()
                .name("Test Product")
                .price(1000)
                .stockQuantity(10)
                .boardwriter("Test Writer")  // 여기에서 boardwriter 설정
                .productname("Test Product Name")
                .build();

        Product savedproduct = productRepository.save(product);
        Product findProduct = productRepository.findById(savedproduct.getId()).orElse(null);

        Assertions.assertThat(findProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testOrder() {

        product = Product.builder()
                .name("Test Product")
                .price(1000)
                .stockQuantity(10)
                .boardwriter("Test Writer")  // 여기에서 boardwriter 설정
                .productname("Test Product Name")
                .build();

        productRepository.save(product);

        OrderItemDto orderItemDto = OrderItemDto.builder()
                .orderPrice(1000)
                .count(2)
                .build();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .city("seoul")
                .zipcode("bong")
                .street("houl")
                .build();

        Delivery city = deliveryRepository.findByCity(deliveryDto.getCity());
        assertEquals("seoul",deliveryDto.getCity());


        orderService.order(orderItemDto, "Test User", product.getUuid(), deliveryDto);
        Optional<Order> optionalOrder = orderRepository.findAll().stream().findFirst();
        assertTrue(optionalOrder.isPresent(), "Order should be present");
        Order order = optionalOrder.get();

        assertEquals(1, order.getOrderItems().size(), "Order should have one order item");
        assertEquals(8, product.getStockQuantity(), "Product stock quantity should be reduced");

    }



}
