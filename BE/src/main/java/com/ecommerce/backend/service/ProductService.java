package com.ecommerce.backend.service;


import com.ecommerce.backend.configuration.JwtRequestFilter;
import com.ecommerce.backend.dao.CartDao;
import com.ecommerce.backend.dao.ProductDao;
import com.ecommerce.backend.dao.UserDao;
import com.ecommerce.backend.entity.Cart432;
import com.ecommerce.backend.entity.Product432;
import com.ecommerce.backend.entity.User432;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    public Product432 addNewProduct(Product432 product) {
        return productDao.save(product);
    }

    public List<Product432> getAllProducts(int pageNumber, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber,12);

        if(searchKey.equals("")) {
            return (List<Product432>) productDao.findAll(pageable);
        } else {
            return (List<Product432>) productDao.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                    searchKey, searchKey, pageable
            );
        }

    }

    public Product432 getProductDetailsById(Integer productId) {
        return productDao.findById(productId).get();
    }

    public void deleteProductDetails(Integer productId) {
        productDao.deleteById(productId);
    }

    public List<Product432> getProductDetails(boolean isSingleProductCheckout, Integer productId) {
        if(isSingleProductCheckout && productId != 0) {
            // we are going to buy a single product

            List<Product432> list = new ArrayList<>();
            Product432 product = productDao.findById(productId).get();
            list.add(product);
            return list;
        } else {
            // we are going to checkout entire cart
            String username = JwtRequestFilter.CURRENT_USER;
            User432 user = userDao.findById(username).get();
            List<Cart432> carts = cartDao.findByUser(user);

            return carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());
        }
    }
}
