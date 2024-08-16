package com.example.jpashop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter
@Setter
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalSales;

    public void addToTotalSales(double amount) {
        this.totalSales += amount;
    }

    public void subtractFromTotalSales(double amount) {
        this.totalSales -= amount;
    }
}