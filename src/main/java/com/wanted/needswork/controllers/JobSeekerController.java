package com.wanted.needswork.controllers;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobSeekerController {
    @Autowired
    JobSeekerService jobSeekerService;
    @GetMapping ("/JobSeeker/showall")
    public ResponseEntity <List<JobSeeker>> showall () {
        return new ResponseEntity<>(JobSeekerService.getJobSeekers(), HttpStatus.OK);
    }
}