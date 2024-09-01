package com.example.jpashop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "sales")
@Getter
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