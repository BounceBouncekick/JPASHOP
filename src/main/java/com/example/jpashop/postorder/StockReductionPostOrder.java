package com.example.jpashop.postorder;

import com.example.jpashop.entity.Order;
import com.example.jpashop.entity.Product;

public class StockReductionPostOrder implements PostOrder {

    private final Product product;
    private final int orderCount;

    public StockReductionPostOrder(Product product, int orderCount) {
        this.product = product;
        this.orderCount = orderCount;
    }

    @Override
    public void execute(Order order) {
        // 재고 차감
        product.removeStock(orderCount);
    }
}
