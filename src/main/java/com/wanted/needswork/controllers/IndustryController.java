package com.wanted.needswork.controllers;

import com.wanted.needswork.models.User;
import com.wanted.needswork.services.IndustryService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndustryController {
    @Autowired
    IndustryService industryService;
    @GetMapping ("/industry/showall")
    public ResponseEntity <List<User>> showall () {
        return new ResponseEntity<>(industryService.getIndustry(), HttpStatus.OK);
    }
}