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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    @PutMapping("/jobSeeker/resume/{jobSeekerId}")
    public void addTextResumeJobSeeker(@RequestParam("file") MultipartFile file, @PathVariable BigInteger jobSeekerId) {
        if (file.isEmpty()) {
            return;
        }

        try {
            // Сохраняем файл в локальный путь
            String currentDir = System.getProperty("user.dir");
            String uploadDir = Paths.get(currentDir, "textResume").toString();
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileExtension = getFileExtension(file.getOriginalFilename());
            String randomFileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = Paths.get(uploadDir, randomFileName).toString();
            File savedFile = new File(filePath);
            file.transferTo(savedFile);

            // Вывод пути сохраненного файла
            System.out.println("Файл сохранен по пути: " + savedFile.getAbsolutePath());
            jobSeekerService.addTextResume(jobSeekerId, savedFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

}