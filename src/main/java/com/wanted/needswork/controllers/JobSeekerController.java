package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.JobSeekerDTO;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobSeekerController {
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    UserService userService;
    @GetMapping ("/jobSeeker/showall")
    public ResponseEntity <List<JobSeeker>> showall () {
        return new ResponseEntity<>(jobSeekerService.getJobSeekers(), HttpStatus.OK);
    }
    @GetMapping ("/jobSeeker/{jobSeekerId}")
    public ResponseEntity <JobSeeker> getJobSeekerById (@PathVariable Integer jobSeekerId) {
        return new ResponseEntity<>(jobSeekerService.getJobSeeker(jobSeekerId), HttpStatus.OK);
    }
    @PostMapping("/jobSeeker")
    public ResponseEntity <JobSeeker> addJobSeeker (@RequestBody JobSeekerDTO jobSeekerDTO) {
        User user = userService.getUser(jobSeekerDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobSeekerService.addJobSeeker(user, jobSeekerDTO.getVideo_cv(),
                jobSeekerDTO.getLatitude(), jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }
    @PutMapping("/jobSeeker/{jobSeekerId}")
    public ResponseEntity <JobSeeker> updateJobSeeker (@RequestBody JobSeekerDTO jobSeekerDTO, @PathVariable Integer jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerService. getJobSeeker(jobSeekerId);
        User user = userService.getUser(jobSeekerDTO.getUser_id());
        if (jobSeeker == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobSeekerService.updateJobSeeker(jobSeeker,user,
                jobSeekerDTO.getVideo_cv(), jobSeekerDTO.getLatitude(),jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }

}