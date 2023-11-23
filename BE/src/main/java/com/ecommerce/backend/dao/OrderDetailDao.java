package com.ecommerce.backend.dao;


import com.ecommerce.backend.entity.OrderDetail432;
import com.ecommerce.backend.entity.User432;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailDao extends CrudRepository<OrderDetail432, Integer> {
    public List<OrderDetail432> findByUser(User432 user);

    public List<OrderDetail432> findByOrderStatus(String status);
}
