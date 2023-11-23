package com.ecommerce.backend.controller;


import com.ecommerce.backend.entity.ImageModel432;
import com.ecommerce.backend.entity.Product432;
import com.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
//@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product432 addNewProduct(@RequestPart("product") Product432 product,
                                 @RequestPart("imageFile") MultipartFile[] file) {
        try {
            Set<ImageModel432> images = uploadImage(file);
            product.setProductImages(images);
            return productService.addNewProduct(product);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Set<ImageModel432> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel432> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles) {
            ImageModel432 imageModel = new ImageModel432(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }

        return imageModels;
    }

    @GetMapping({"/getAllProducts"})
    public List<Product432> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(defaultValue = "") String searchKey) {
        List<Product432> result = productService.getAllProducts(pageNumber, searchKey);
        System.out.println("Result size is "+ result.size());
        return result;
    }

    @GetMapping({"/getProductDetailsById/{productId}"})
    public Product432 getProductDetailsById(@PathVariable("productId") Integer productId) {
        return productService.getProductDetailsById(productId);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public void deleteProductDetails(@PathVariable("productId") Integer productId) {
        productService.deleteProductDetails(productId);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
    public List<Product432> getProductDetails(@PathVariable(name = "isSingleProductCheckout" ) boolean isSingleProductCheckout,
                                           @PathVariable(name = "productId")  Integer productId) {
        return productService.getProductDetails(isSingleProductCheckout, productId);
    }
}
