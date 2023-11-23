package com.ecommerce.backend.service;


import com.ecommerce.backend.dao.RoleDao;
import com.ecommerce.backend.dao.UserDao;
import com.ecommerce.backend.entity.Role432;
import com.ecommerce.backend.entity.User432;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public void initRoleAndUser() {
        Role432 adminRole = new Role432();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);
        
        Role432 userRole = new Role432();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDao.save(userRole);
        
         User432 adminUser = new User432();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        Set<Role432> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);
        
        User432 user = new User432();
        user.setUserName("alune");
        user.setUserPassword(getEncodedPassword("minh2001"));
        user.setUserFirstName("minh");
        user.setUserLastName("ngo");
        Set<Role432> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userDao.save(user);
    }

    public User432 registerNewUser(User432 user) {
        Role432 role = roleDao.findById("User").get();
        Set<Role432> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        return userDao.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
