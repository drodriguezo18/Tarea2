package com.project.demo.logic.entity.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.demo.logic.entity.categoryList.CategoryList;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name= "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_list_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("categories")
    private CategoryList categoryList;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryList getCategoryList() {
        return categoryList;
    }
    public void setCategoryList(CategoryList categoryList) {
        this.categoryList = categoryList;
    }


}
