package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Operation (summary = "отображение всех пользователей" )
    @GetMapping ("/user/all")
    public ResponseEntity <List<User>> showAll () {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping ("/user/{userId}")
    public ResponseEntity <User> getUserByID (@PathVariable BigInteger userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);

    }

    @PostMapping ("/user")
    public ResponseEntity <User> addUser (@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.addUser(userDTO.getFullName(), null, userDTO.getUsername(), userDTO.getID()),
                HttpStatus.OK);
    }
    @PutMapping ("/user")
    public ResponseEntity <User> updateUser (@RequestBody UserDTO userDTO) {
        User user = userService. getUser(userDTO.getID());
        if (user == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.updateUser(user, userDTO.getFullName(), userDTO.getPhone(), userDTO.getUsername()),
                HttpStatus.OK);
    }

}
