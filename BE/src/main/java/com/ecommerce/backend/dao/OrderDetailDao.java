package com.ecommerce.backend.dao;


import com.ecommerce.backend.entity.OrderDetail;
import com.ecommerce.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailDao extends CrudRepository<OrderDetail, Integer> {
    public List<OrderDetail> findByUser(User user);

    public List<OrderDetail> findByOrderStatus(String status);
}
