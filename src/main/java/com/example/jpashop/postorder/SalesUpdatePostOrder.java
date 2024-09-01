package com.example.jpashop.postorder;

import com.example.jpashop.entity.Order;
import com.example.jpashop.entity.Sales;
import com.example.jpashop.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalesUpdatePostOrder implements PostOrder {

    private final SalesRepository salesRepository;

    public SalesUpdatePostOrder(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @Override
    public void execute(Order order) {
        // 판매 금액 업데이트
        Sales sales = salesRepository.findById(1L).orElse(new Sales());
        sales.addToTotalSales(order.getTotalPrice());
        salesRepository.save(sales);
        log.info("Sales updated for order: {}", order.getId());
    }
}
