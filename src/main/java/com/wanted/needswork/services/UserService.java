package com.wanted.needswork.services;

import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class UserService
{
    @Autowired
    UserRepository userRepository;
    public List <User> getUsers () {
        return userRepository.findAll();
    }

    public User getUser(BigInteger userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User addUser(String fullName, String username, BigInteger ID){
        User user = new User(ID, fullName, username);
        if (getUser(ID)==null) {
            return getUser(ID);
        }
        return userRepository.save(user);
    }

    public User updateUser(User user, String fullName, String phone, String username) {
        if (fullName != null) {
        user.setFullName(fullName);
        }
        if (phone != null) {
        user.setPhone(phone);
        }
        if (username != null) {
            user.setUsername(username);
        }
        return userRepository.save(user);
    }
}
