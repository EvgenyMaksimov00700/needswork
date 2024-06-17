package com.wanted.needswork.services;

import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService
{
    @Autowired
    UserRepository userRepository;
    public List <User> getUsers () {
        return userRepository.findAll();
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User addUser(String fullName, String phone, String username){
        User user = new User(fullName, phone,username);
        return userRepository.save(user);
    }
}
