package com.example.jpashop.entity;

import com.example.jpashop.dto.ProductDto;
import com.example.jpashop.exception.NotEnoughStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "shop_Product" )
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Column(length = 20, nullable = false)
    @NotNull
    private String boardwriter;

    private String productname;

    private String uuid = UUID.randomUUID().toString();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Product(String name, int price, int stockQuantity, String boardwriter, String productname) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.boardwriter = boardwriter;
        this.productname = productname;
    }



    public static Product toDTO(ProductDto productDTO){
        return Product.builder()
                .boardwriter(productDTO.getBoardwriter())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .productname(productDTO.getProductname())
                .build();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stockQuantity +
                ", boardWriter =" + boardwriter +
                ", productname =" + productname +
                '}';
    }

    //재고 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
