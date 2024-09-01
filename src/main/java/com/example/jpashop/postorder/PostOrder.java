package com.example.jpashop.postorder;

import com.example.jpashop.entity.Order;
import java.util.concurrent.CompletableFuture;

public interface PostOrder {
    void execute(Order order);

    default CompletableFuture<Void> executeAsync(Order order) {
        return CompletableFuture.runAsync(() -> execute(order));
    }
}
