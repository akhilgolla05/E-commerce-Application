package com.learnboot.dreamshopping.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private BigDecimal price;

    public OrderItem(Order order, int quantity, Product product, BigDecimal price) {
        this.order = order;
        this.quantity = quantity;
        this.product = product;
        this.price = price;
    }
}
