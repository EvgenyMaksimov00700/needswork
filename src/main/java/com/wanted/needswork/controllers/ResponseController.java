package com.wanted.needswork.controllers;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResponseController {
    @Autowired
    ResponseService responseService;
    @GetMapping ("/Response/showall")
    public ResponseEntity <List<Response>> showall () {
        return new ResponseEntity<>(ResponseService.getResponse(), HttpStatus.OK);
    }
}