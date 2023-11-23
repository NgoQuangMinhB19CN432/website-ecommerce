package com.ecommerce.backend.dao;

import com.ecommerce.backend.entity.Role432;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role432, String> {

}
