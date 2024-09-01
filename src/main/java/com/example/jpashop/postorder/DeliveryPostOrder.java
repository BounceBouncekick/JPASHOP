package com.example.jpashop.postorder;

import com.example.jpashop.dto.DeliveryDto;
import com.example.jpashop.entity.Delivery;
import com.example.jpashop.entity.Order;
import com.example.jpashop.repository.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryPostOrder implements PostOrder {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryDto deliveryDto;

    public DeliveryPostOrder(DeliveryRepository deliveryRepository, DeliveryDto deliveryDto) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryDto = deliveryDto;
    }

    @Override
    public void execute(Order order) {
        // 배송 정보 생성 및 저장
        Delivery delivery = Delivery.createDelivery(deliveryDto);
        deliveryRepository.save(delivery);
        log.info("Delivery created for order: {}", order.getId());
    }
}
