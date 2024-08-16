package com.example.jpashop.controller;


import com.example.jpashop.dto.OrderRequestDto;
import com.example.jpashop.service.Orderservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final Orderservice orderservice;

    @PostMapping("/orders/{uuid}/order")
    public ResponseEntity<String> order(@RequestBody OrderRequestDto orderRequestDto,
                                        @RequestParam("name") String name, @PathVariable("uuid") String uuid)throws IOException {


        log.info("Controller_name : {}",name);
        log.info("Controller_uuid : {}",uuid);

        orderservice.order(orderRequestDto.getOrderItem(), name, uuid, orderRequestDto.getDelivery());
        return ResponseEntity.ok("주문되었습니다.");

    }

    @PostMapping("/orders/{uuid}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable("uuid") String uuid) {
        orderservice.cancelOrder(uuid);
        return ResponseEntity.ok("취소되었습니다.");
    }
}