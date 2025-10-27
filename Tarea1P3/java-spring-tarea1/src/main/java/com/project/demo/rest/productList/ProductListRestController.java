package com.project.demo.rest.productList;


import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.productList.ProductList;
import com.project.demo.logic.entity.productList.ProductListRepository;
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
@RequestMapping("/product-lists")
public class ProductListRestController {
    @Autowired
    private ProductListRepository productListRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<ProductList> ordersPage = productListRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Product List retrieved successfully",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addProductList(@RequestBody ProductList gifList, HttpServletRequest request) {
        ProductList savedOrder = productListRepository.save(gifList);
        return new GlobalResponseHandler().handleResponse("Product list created successfully",
                savedOrder, HttpStatus.CREATED, request);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> editProductList(@RequestBody ProductList gifList, HttpServletRequest request) {
        ProductList savedOrder = productListRepository.save(gifList);
        return new GlobalResponseHandler().handleResponse("Product list created successfully",
                savedOrder, HttpStatus.CREATED, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        Optional<ProductList> foundItem = productListRepository.findById(id);
        if(foundItem.isPresent()) {
            productListRepository.deleteById(foundItem.get().getId());
            return new GlobalResponseHandler().handleResponse("Product List deleted successfully",
                    foundItem.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Product List " + id + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/{productListId}/products")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addGiftToGiftList(@PathVariable Long productListId, @RequestBody Product gift, HttpServletRequest request) {
        Optional<ProductList> foundGiftList = productListRepository.findById(productListId);
        if(foundGiftList.isPresent()) {
            ProductList giftList = foundGiftList.get();
            gift.setProductList(giftList);
            Product savedGift = productRepository.save(gift);
            return new GlobalResponseHandler().handleResponse("Product added to Product List successfully",
                    savedGift, HttpStatus.CREATED, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Product List " + productListId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/{productListId}/products")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getProductsFromProductsList(
            @PathVariable Long productListId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Optional<ProductList> foundGiftList = productListRepository.findById(productListId);
        if(foundGiftList.isPresent()) {
            Pageable pageable = PageRequest.of(page-1, size);
            Page<Product> giftsPage = productRepository.findByProductListId(productListId, pageable);

            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(giftsPage.getTotalPages());
            meta.setTotalElements(giftsPage.getTotalElements());
            meta.setPageNumber(giftsPage.getNumber() + 1);
            meta.setPageSize(giftsPage.getSize());

            return new GlobalResponseHandler().handleResponse("Products from Product List retrieved successfully",
                    giftsPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Product List " + productListId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{productListId}/products/{productId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> removeProductfromProductList(@PathVariable Long productListId, @PathVariable Long productId, HttpServletRequest request) {
        Optional<ProductList> foundGiftList = productListRepository.findById(productListId);
        Optional<Product> foundGift = productRepository.findById(productId);

        if(foundGiftList.isPresent() && foundGift.isPresent()) {
            Product gift = foundGift.get();
            if(gift.getProductList().getId().equals(productListId)) {
                productRepository.deleteById(productId);
                return new GlobalResponseHandler().handleResponse("Gift removed from Gift List successfully",
                        gift, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Product " + productId + " does not belong to Product List " + productListId,
                        HttpStatus.BAD_REQUEST, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Product List or Product not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
