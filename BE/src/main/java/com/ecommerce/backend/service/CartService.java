package com.ecommerce.backend.service;


import com.ecommerce.backend.configuration.JwtRequestFilter;
import com.ecommerce.backend.dao.*;
import com.ecommerce.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    public void deleteCartItem(Integer cartId) {
        cartDao.deleteById(cartId);
    }

    public Cart432 addToCart(Integer productId) {
        Product432 product = productDao.findById(productId).get();

        String username = JwtRequestFilter.CURRENT_USER;

        User432 user = null;
        if(username != null) {
            user = userDao.findById(username).get();
        }

        List<Cart432> cartList = cartDao.findByUser(user);
        List<Cart432> filteredList = cartList.stream().filter(x -> Objects.equals(x.getProduct().getProductId(), productId)).collect(Collectors.toList());

        if(!filteredList.isEmpty()) {
            return null;
        }

        if(product != null && user != null) {
            Cart432 cart = new Cart432(product, user);
            return cartDao.save(cart);
        }

        return null;
    }

    public List<Cart432> getCartDetails() {
        String username = JwtRequestFilter.CURRENT_USER;
        User432 user = userDao.findById(username).get();
        return cartDao.findByUser(user);
    }
}
