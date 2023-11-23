package com.ecommerce.backend.dao;


import com.ecommerce.backend.entity.Cart432;
import com.ecommerce.backend.entity.User432;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends CrudRepository<Cart432, Integer > {
    public List<Cart432> findByUser(User432 user);
}
