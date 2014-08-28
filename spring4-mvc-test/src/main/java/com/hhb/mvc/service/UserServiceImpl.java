package com.hhb.mvc.service;


import org.springframework.stereotype.Service;

import com.hhb.mvc.entity.User;

@Service
public class UserServiceImpl implements UserService {

    public User findById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("zhang");
        return user;
    }
}
