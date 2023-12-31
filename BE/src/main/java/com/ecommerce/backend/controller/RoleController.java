package com.ecommerce.backend.controller;


import com.ecommerce.backend.entity.Role432;
import com.ecommerce.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    public Role432 createNewRole(@RequestBody Role432 role) {
        return roleService.createNewRole(role);
    }
}
