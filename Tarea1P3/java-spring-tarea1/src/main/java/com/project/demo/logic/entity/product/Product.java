package com.project.demo.logic.entity.product;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.productList.ProductList;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Table(name = "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private int price;
    private Double stock;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_list_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("products")

    private ProductList productList;


    public Product() {}


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public Double getStock() {
        return stock;
    }
    public void setStock(Double stock) {this.stock = stock;}

    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

}
