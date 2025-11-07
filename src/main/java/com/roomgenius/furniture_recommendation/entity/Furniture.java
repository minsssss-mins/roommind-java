package com.roomgenius.furniture_recommendation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String style;
    private double price;

    public Furniture(String name, String style, double price) {
        this.name = name;
        this.style = style;
        this.price = price;
    }
}
