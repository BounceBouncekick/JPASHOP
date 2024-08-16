package com.example.jpashop.dto;

import com.example.jpashop.entity.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderItemDto {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private int orderPrice; //주문가격

    private int count; //주문 수량

    private DeliveryDto deliveryDto;

    @Builder
    public  OrderItemDto(int orderPrice, int count, DeliveryDto deliveryDto) {
        this.orderPrice = orderPrice;
        this.count = count;
        this.deliveryDto = deliveryDto;
    }

    public static OrderItemDto toEntity(OrderItem orderItem){
        return OrderItemDto.builder()
                .orderPrice(orderItem.getOrderPrice())
                .count(orderItem.getCount())
                .build();
    }
}

