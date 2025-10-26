package com.project.demo.logic.entity.categoryList;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.category.Category;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "category_list")
@Entity
public class CategoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "categoryList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("categoryList")
    private List<Category> categories = new ArrayList<>();

    public CategoryList() {
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
