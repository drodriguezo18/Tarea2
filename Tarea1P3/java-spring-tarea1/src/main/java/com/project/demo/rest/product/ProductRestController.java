package com.project.demo.rest.product;

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
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductListRepository productListRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Product> productsPage = productRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(productsPage.getTotalPages());
        meta.setTotalElements(productsPage.getTotalElements());
        meta.setPageNumber(productsPage.getNumber() + 1);
        meta.setPageSize(productsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Products retrieved successfully",
                productsPage.getContent(), HttpStatus.OK, meta);

    }

    @GetMapping("/product-list/{productListId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getGiftsByGiftListId(
            @PathVariable Long productListId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Product> giftsPage = productRepository.findByProductListId(productListId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(giftsPage.getTotalPages());
        meta.setTotalElements(giftsPage.getTotalElements());
        meta.setPageNumber(giftsPage.getNumber() + 1);
        meta.setPageSize(giftsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Products retrieved successfully",
                giftsPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Product> foundGift = productRepository.findById(id);
        if(foundGift.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Product retrieved successfully",
                    foundGift.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Product " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody Product gift, HttpServletRequest request) {
        if (gift.getProductList() != null && gift.getProductList().getId() != null) {
            Optional<ProductList> giftList = productListRepository.findById(gift.getProductList().getId());
            if (giftList.isPresent()) {
                gift.setProductList(giftList.get());
                Product savedGift = productRepository.save(gift);
                return new GlobalResponseHandler().handleResponse("Product created successfully",
                        savedGift, HttpStatus.CREATED, request);
            } else {
                return new GlobalResponseHandler().handleResponse("ProductList not found",
                        HttpStatus.BAD_REQUEST, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("ProductList is required",
                    HttpStatus.BAD_REQUEST, request);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product gift, HttpServletRequest request) {
        Optional<Product> existingGift = productRepository.findById(id);
        if (existingGift.isPresent()) {
            Product giftToUpdate = existingGift.get();
            giftToUpdate.setName(gift.getName());
            giftToUpdate.setPrice(gift.getPrice());
            giftToUpdate.setStock(gift.getStock());

            if (gift.getProductList() != null && gift.getProductList().getId() != null) {
                Optional<ProductList> giftList = productListRepository.findById(gift.getProductList().getId());
                if (giftList.isPresent()) {
                    giftToUpdate.setProductList(giftList.get());
                } else {
                    return new GlobalResponseHandler().handleResponse("GiftList not found",
                            HttpStatus.BAD_REQUEST, request);
                }
            }

            Product savedGift = productRepository.save(giftToUpdate);
            return new GlobalResponseHandler().handleResponse("Product updated successfully",
                    savedGift, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Product " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        Optional<Product> foundGift = productRepository.findById(id);
        if(foundGift.isPresent()) {
            productRepository.deleteById(foundGift.get().getId());
            return new GlobalResponseHandler().handleResponse("Product deleted successfully",
                    foundGift.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Product " + id + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


}
