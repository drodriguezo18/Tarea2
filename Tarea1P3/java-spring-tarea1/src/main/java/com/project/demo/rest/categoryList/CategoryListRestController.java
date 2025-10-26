package com.project.demo.rest.categoryList;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.categoryList.CategoryList;
import com.project.demo.logic.entity.categoryList.CategoryListRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
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
@RequestMapping("/category-lists")
public class CategoryListRestController {

    @Autowired
    private CategoryListRepository categoryListRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<CategoryList> ordersPage = categoryListRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Category list retrieved successfully",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    public ResponseEntity<?> addCategoryList(@RequestBody CategoryList gifList, HttpServletRequest request) {
        CategoryList savedOrder = categoryListRepository.save(gifList);
        return new GlobalResponseHandler().handleResponse("Category list created successfully",
                savedOrder, HttpStatus.CREATED, request);
    }

    @PutMapping
    public ResponseEntity<?> editCategoryList(@RequestBody CategoryList gifList, HttpServletRequest request) {
        CategoryList savedOrder = categoryListRepository.save(gifList);
        return new GlobalResponseHandler().handleResponse("Category list created successfully",
                savedOrder, HttpStatus.CREATED, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, HttpServletRequest request) {
        Optional<CategoryList> foundItem = categoryListRepository.findById(id);
        if(foundItem.isPresent()) {
            categoryListRepository.deleteById(foundItem.get().getId());
            return new GlobalResponseHandler().handleResponse("Category List deleted successfully",
                    foundItem.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Category List " + id + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/{categoryListId}/categories")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addCategorytoCategoryList(@PathVariable Long categoryListId, @RequestBody Category category, HttpServletRequest request) {
        Optional<CategoryList> foundCategoryList = categoryListRepository.findById(categoryListId);
        if(foundCategoryList.isPresent()) {
            CategoryList categoryList = foundCategoryList.get();
            category.setCategoryList(categoryList);
            Category savedCategory = categoryRepository.save(category);
            return new GlobalResponseHandler().handleResponse("Category added to Category List successfully",
                    savedCategory, HttpStatus.CREATED, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Category List " + categoryListId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/{categoryListId}/categories")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCategoriesfromCateogryList(
            @PathVariable Long categoryListId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Optional<CategoryList> foundCategoryList = categoryListRepository.findById(categoryListId);
        if(foundCategoryList.isPresent()) {
            Pageable pageable = PageRequest.of(page-1, size);
            Page<Category> giftsPage = categoryRepository.findByCategoryListId(categoryListId, pageable);

            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(giftsPage.getTotalPages());
            meta.setTotalElements(giftsPage.getTotalElements());
            meta.setPageNumber(giftsPage.getNumber() + 1);
            meta.setPageSize(giftsPage.getSize());

            return new GlobalResponseHandler().handleResponse("Categories from Category List retrieved successfully",
                    giftsPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Category List " + categoryListId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{categoryListId}/categories/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeCategoriesfromCategoriesList(@PathVariable Long categoryListId, @PathVariable Long categoryId, HttpServletRequest request) {
        Optional<CategoryList> foundGiftList = categoryListRepository.findById(categoryListId);
        Optional<Category> foundGift = categoryRepository.findById(categoryId);

        if(foundGiftList.isPresent() && foundGift.isPresent()) {
            Category gift = foundGift.get();
            if(gift.getCategoryList().getId().equals(categoryListId)) {
                categoryRepository.deleteById(categoryId);
                return new GlobalResponseHandler().handleResponse("Category removed from Category List successfully",
                        gift, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Category " + categoryId + " does not belong to Category List " + categoryListId,
                        HttpStatus.BAD_REQUEST, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Category List or Category not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

}
