package com.ecommerce.backend.service;


import com.ecommerce.backend.dao.RoleDao;
import com.ecommerce.backend.entity.Role432;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role432 createNewRole(Role432 role) {
        return roleDao.save(role);
    }
}
