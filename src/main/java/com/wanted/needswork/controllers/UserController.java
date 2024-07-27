package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity <List<UserResponseDTO>> showAll () {
        List<User> users = userService.getUsers();
        List<UserResponseDTO> userResponseDTOs = new java.util.ArrayList<>();
        for (User user : users) {
            userResponseDTOs.add(user.toResponseDTO());
        }
        return new ResponseEntity<>(userResponseDTOs, HttpStatus.OK);

    }

    @GetMapping ("/user/{userId}")
    public ResponseEntity <UserResponseDTO> getUserByID (@PathVariable BigInteger userId) {
        return new ResponseEntity<>(userService.getUser(userId).toResponseDTO(), HttpStatus.OK);

    }

    @PostMapping ("/user")
    public ResponseEntity <User> addUser (@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.addUser(userDTO.getFullName(), userDTO.getUsername(), userDTO.getId()),
                HttpStatus.OK);
    }
    @PutMapping ("/user")
    public ResponseEntity <User> updateUser (@RequestBody UserDTO userDTO) {
        User user = userService. getUser(userDTO.getId());
        if (user == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.updateUser(user, userDTO.getFullName(), userDTO.getPhone(), userDTO.getUsername()),
                HttpStatus.OK);
    }

}
