package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.JobSeekerDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.JobSeekerResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class JobSeekerController {
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    UserService userService;

    @GetMapping("/jobSeeker/showall")
    public ResponseEntity<List<JobSeekerResponseDTO>> showall() {
        List<JobSeeker> jobSeekers = jobSeekerService.getJobSeekers();
        List<JobSeekerResponseDTO> jobSeekerResponseDTOs = new java.util.ArrayList<>();
        for (JobSeeker jobSeeker : jobSeekers) {
            jobSeekerResponseDTOs.add(jobSeeker.toResponseDTO());
        }


        return new ResponseEntity<>(jobSeekerResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/jobSeeker/{jobSeekerId}")
    public ResponseEntity<JobSeekerResponseDTO> getJobSeekerById(@PathVariable Integer jobSeekerId) {
        return new ResponseEntity<>(jobSeekerService.getJobSeeker(jobSeekerId).toResponseDTO(), HttpStatus.OK);
    }

    @PostMapping("/jobSeeker")
    public ResponseEntity<JobSeeker> addJobSeeker(@RequestBody JobSeekerDTO jobSeekerDTO) {
        User user = userService.getUser(jobSeekerDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobSeekerService.addJobSeeker(user,
                jobSeekerDTO.getLatitude(), jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }

    @PutMapping("/jobSeeker/{jobSeekerId}")
    public ResponseEntity<JobSeeker> updateJobSeeker(@RequestBody JobSeekerDTO jobSeekerDTO, @PathVariable Integer jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(jobSeekerId);
        User user = userService.getUser(jobSeekerDTO.getUser_id());
        if (jobSeeker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jobSeekerService.updateJobSeeker(jobSeeker, user,
                jobSeekerDTO.getLatitude(), jobSeekerDTO.getLongitude()),
                HttpStatus.OK);
    }

    @GetMapping("/jobSeeker/user/{userId}")
    public ResponseEntity<JobSeekerResponseDTO> getJobSeekerByUserId(@PathVariable BigInteger userId) {
        return new ResponseEntity<>(jobSeekerService.getJobSeekerByUserId(userId).toResponseDTO(), HttpStatus.OK);
    }

    @PostMapping("/jobSeeker/resume/{jobSeekerId}")
    public ResponseEntity<JobSeeker> addTextResumeJobSeeker(@RequestBody JobSeekerDTO jobSeekerDTO, @PathVariable Integer jobSeekerId) {
        return null;
    }

}