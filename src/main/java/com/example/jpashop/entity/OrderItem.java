package com.example.jpashop.entity;

import com.example.jpashop.dto.OrderItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import static jakarta.persistence.FetchType.LAZY;

@Slf4j
@Getter
@Entity
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_OrderItem")
    private Long id;

    private int orderPrice; //주문가격

    private int count; //주문 수량

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public OrderItem(int orderPrice, int count,Product product) {
        this.orderPrice = orderPrice;
        this.product = product;
        this.count = count;
    }

    public OrderItem(Product product) {
        this.product = product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public static OrderItem toDTO(OrderItemDto orderItemDTO){
        return OrderItem.builder()
                .orderPrice(orderItemDTO.getOrderPrice())
                .count(orderItemDTO.getCount())
                .build();
    }
    public static OrderItem createOrderItem(Product product, OrderItemDto orderItemDTO) {
        log.info("createOrderItem : {}", product);
        log.info("createOrderItem2 : {}", orderItemDTO);
        log.info("ItemCount : {}",orderItemDTO.getCount());
        return OrderItem.builder()
                .product(product)
                .orderPrice(orderItemDTO.getOrderPrice())
                .count(orderItemDTO.getCount())
                .build();
    }
    public void cancel(){
        getProduct().addStock(count);
    }
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}