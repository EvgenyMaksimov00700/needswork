package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.JobSeekerDTO;
import com.wanted.needswork.DTO.request.VideoCvDTO;
import com.wanted.needswork.DTO.request.VideoCvSendDTO;
import com.wanted.needswork.DTO.response.JobSeekerResponseDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import com.wanted.needswork.DTO.response.VideoCvResponseDTO;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.models.VideoCv;
import com.wanted.needswork.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
@RestController()
public class VideoCvController {
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    VideoCvService videoCvService;

    @Autowired
    UserService userService;

    @Autowired
    VacancyService vacancyService;
    @Autowired
    HHService hhService;


    @GetMapping("/videoCv/showall")
    public ResponseEntity<List<VideoCvResponseDTO>> showall() {
        List<VideoCv> videoCvs = videoCvService.getVideoCv();
        List<VideoCvResponseDTO> videoCvResponseDTOs = new java.util.ArrayList<>();
        for (VideoCv videoCv : videoCvs) {
            videoCvResponseDTOs.add(videoCv.toResponseDTO());
        }


        return new ResponseEntity<>(videoCvResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/videoCv/{videoCvId}")
    public ResponseEntity<VideoCvResponseDTO> getVideoCvById(@PathVariable Integer videoCvId) {
        return new ResponseEntity<>(videoCvService.getVideoCv(videoCvId).toResponseDTO(), HttpStatus.OK);
    }

    @PostMapping("/videoCv")
    public ResponseEntity<VideoCv> addVideoCv(@RequestBody VideoCvDTO videoCvDTO) {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(videoCvDTO.getJob_seeker_id());
        if (jobSeeker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videoCvService.addVideoCv(jobSeeker,
                videoCvDTO.getVideo_message(), videoCvDTO.getName()),
                HttpStatus.OK);
    }

    @PutMapping("/videoCv/{videoCvId}")
    public ResponseEntity<VideoCv> updateVideoCv(@RequestBody VideoCvDTO videoCvDTO, @PathVariable Integer VideoCvId) {
        VideoCv videoCv = videoCvService.getVideoCv(VideoCvId);
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(videoCvDTO.getJob_seeker_id());
        if (jobSeeker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videoCvService.updateVideoCv(videoCv, jobSeeker,
                videoCvDTO.getVideo_message(), videoCvDTO.getName()),
                HttpStatus.OK);
    }

    @GetMapping("/videoCv/user/{userId}")
    public ResponseEntity<List<VideoCvResponseDTO>> getVideoCvByUserId(@PathVariable BigInteger userId) {
        User user = userService.getUser(userId);
        JobSeeker jobSeeker = jobSeekerService.getJobSeekerByUserId(user.getId());
        List<VideoCv> cvs = videoCvService.getVideoCvByUser(jobSeeker);
        return new ResponseEntity<>(cvs.stream().map(VideoCv::toResponseDTO).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/videoCv/{videoCvId}")
    public ResponseEntity<VideoCvResponseDTO> deleteVideoCvByID(@PathVariable Integer videoCvId) {
        videoCvService.deleteVideoCv(videoCvId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/videoCv/send")
    public ResponseEntity<VideoCv> VideoCvSend (@RequestBody VideoCvSendDTO videoCvSendDTO) {
        User user = userService.getUser(videoCvSendDTO.getUserId());
        Vacancy vacancy = vacancyService.getVacancy(videoCvSendDTO.getVacancyId());
        if (vacancy==null) {
            vacancy = hhService.fetchVacancy(videoCvSendDTO.getVacancyId());
        }
        if (vacancy == null){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        videoCvService.sendVideoNote(user, videoCvSendDTO.getVideoCvMessage(), vacancy, videoCvSendDTO.getTextResume());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
