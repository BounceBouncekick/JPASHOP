package com.example.jpashop.entity;

import com.example.jpashop.em.OrderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shop_orders")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @CreationTimestamp
    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태

    private double totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 10)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Order와 Delivery의 일대일 관계
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private String uuid = UUID.randomUUID().toString();


    @Builder
    public Order(LocalDateTime orderDate, OrderStatus status) {
        this.orderDate = LocalDateTime.now();
        this.status = status;
        this.uuid = UUID.randomUUID().toString(); // UUID 생성
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Product productName, OrderItem... orderItems) {
        Order order = Order.builder()
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.calculateTotalPrice(); // 총 금액 계산 및 설정

        return order;
    }
    public void cancel() {
        this.status = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

        this.totalPrice = 0;
    }
    public void calculateTotalPrice() {
        totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}

