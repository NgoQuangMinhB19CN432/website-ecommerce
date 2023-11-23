package com.ecommerce.backend.dao;

import com.ecommerce.backend.entity.User432;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User432, String> {
}
