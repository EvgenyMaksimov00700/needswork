package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.JobSeekerDTO;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.services.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobSeekerController {
    @Autowired
    JobSeekerService jobSeekerService;
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
        return new ResponseEntity<>(jobSeekerService.addJobSeeker(jobSeekerDTO.getUser_id(), jobSeekerDTO.getVideo_cv(),
                jobSeekerDTO.getLatitude(), jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }
    @PutMapping("/jobSeeker/{jobSeekerId}")
    public ResponseEntity <JobSeeker> updateJobSeeker (@RequestBody JobSeekerDTO jobSeekerDTO, @PathVariable Integer jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerService. getJobSeeker(jobSeekerId);
        if (jobSeeker == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobSeekerService.updateJobSeeker(jobSeeker, jobSeekerDTO.getUser_id(),
                jobSeekerDTO.getVideo_cv(), jobSeekerDTO.getLatitude(),jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }

}