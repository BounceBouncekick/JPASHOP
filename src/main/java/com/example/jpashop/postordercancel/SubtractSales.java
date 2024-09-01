package com.example.jpashop.postordercancel;

import com.example.jpashop.entity.Sales;
import com.example.jpashop.postorder.PostOrderCancelSales;
import com.example.jpashop.repository.SalesRepository;

public class SubtractSales implements PostOrderCancelSales {

    private final SalesRepository salesRepository;

    public SubtractSales(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @Override
    public void execute(double amount) {
        Sales sales = salesRepository.findById(1L).orElse(new Sales());
        sales.subtractFromTotalSales(amount);
        salesRepository.save(sales);  // 매출 정보 저장
    }
}
