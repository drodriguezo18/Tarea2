package com.project.demo.logic.entity.productList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "product_list")
@Entity
public class ProductList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "productList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("productList")
    private List<Product> products = new ArrayList<>();

    public ProductList() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
