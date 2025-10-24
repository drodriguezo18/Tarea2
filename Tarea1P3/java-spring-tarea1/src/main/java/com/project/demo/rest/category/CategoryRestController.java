package com.project.demo.rest.category;


import com.project.demo.logic.entity.categoryList.CategoryList;
import com.project.demo.logic.entity.categoryList.CategoryListRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryRestController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryListRepository categoryListRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(categoriesPage.getTotalPages());
        meta.setTotalElements(categoriesPage.getTotalElements());
        meta.setPageNumber(categoriesPage.getNumber() + 1);
        meta.setPageSize(categoriesPage.getSize());

        return new GlobalResponseHandler().handleResponse("Categories retrieved successfully",
                categoriesPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/category-list/{categoryListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCategorybyCategortListId(
            @PathVariable Long categoryListId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Category> giftsPage = categoryRepository.findByCategoryListId(categoryListId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(giftsPage.getTotalPages());
        meta.setTotalElements(giftsPage.getTotalElements());
        meta.setPageNumber(giftsPage.getNumber() + 1);
        meta.setPageSize(giftsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Categories retrieved successfully",
                giftsPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Category> foundGift = categoryRepository.findById(id);
        if(foundGift.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Category retrieved successfully",
                    foundGift.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Category " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody Category gift, HttpServletRequest request) {
        if (gift.getCategoryList() != null && gift.getCategoryList().getId() != null) {
            Optional<CategoryList> giftList = categoryListRepository.findById(gift.getCategoryList().getId());
            if (giftList.isPresent()) {
                gift.setCategoryList(giftList.get());
                Category savedGift = categoryRepository.save(gift);
                return new GlobalResponseHandler().handleResponse("Category created successfully",
                        savedGift, HttpStatus.CREATED, request);
            } else {
                return new GlobalResponseHandler().handleResponse("CategoryList not found",
                        HttpStatus.BAD_REQUEST, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("CategoryList is required",
                    HttpStatus.BAD_REQUEST, request);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category gift, HttpServletRequest request) {
        Optional<Category> existingGift = categoryRepository.findById(id);
        if (existingGift.isPresent()) {
            Category giftToUpdate = existingGift.get();
            giftToUpdate.setName(gift.getName());
            giftToUpdate.setDescription(gift.getDescription());
            if (gift.getCategoryList() != null && gift.getCategoryList().getId() != null) {
                Optional<CategoryList> giftList = categoryListRepository.findById(gift.getCategoryList().getId());
                if (giftList.isPresent()) {
                    giftToUpdate.setCategoryList(giftList.get());
                } else {
                    return new GlobalResponseHandler().handleResponse("CategoryList not found",
                            HttpStatus.BAD_REQUEST, request);
                }
            }

            Category savedGift = categoryRepository.save(giftToUpdate);
            return new GlobalResponseHandler().handleResponse("Category updated successfully",
                    savedGift, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Category " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        Optional<Category> foundGift = categoryRepository.findById(id);
        if(foundGift.isPresent()) {
            categoryRepository.deleteById(foundGift.get().getId());
            return new GlobalResponseHandler().handleResponse("Category deleted successfully",
                    foundGift.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Category " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
