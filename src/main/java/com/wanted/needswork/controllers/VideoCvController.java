package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.JobSeekerDTO;
import com.wanted.needswork.DTO.request.VideoCvDTO;
import com.wanted.needswork.DTO.response.JobSeekerResponseDTO;
import com.wanted.needswork.DTO.response.VideoCvResponseDTO;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.VideoCv;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.UserService;
import com.wanted.needswork.services.VideoCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller()
public class VideoCvController {
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    VideoCvService videoCvService;
    @GetMapping("/videoCv/showall")
    public ResponseEntity<List<VideoCvResponseDTO>> showall () {
        List<VideoCv> videoCvs = videoCvService.getVideoCv();
        List <VideoCvResponseDTO> videoCvResponseDTOs = new java.util.ArrayList<>();
        for (VideoCv videoCv : videoCvs) {
            videoCvResponseDTOs.add(videoCv.toResponseDTO());
        }


        return new ResponseEntity<>(videoCvResponseDTOs, HttpStatus.OK);
    }
    @GetMapping ("/videoCv/{videoCvrId}")
    public ResponseEntity <VideoCvResponseDTO> getVideoCvById (@PathVariable Integer videoCvId) {
        return new ResponseEntity<>(videoCvService.getVideoCv(videoCvId).toResponseDTO(), HttpStatus.OK);
    }
    @PostMapping("/videoCv")
    public ResponseEntity <VideoCv> addVideoCv (@RequestBody VideoCvDTO videoCvDTO) {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(videoCvDTO.getJob_seeker_id());
        if (jobSeeker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videoCvService.addVideoCv(jobSeeker,
                videoCvDTO.getVideo_message(), videoCvDTO.getName()),
                HttpStatus.OK);
    }
    @PutMapping("/videoCv/{videoCvId}")
    public ResponseEntity <VideoCv> updateVideoCv (@RequestBody VideoCvDTO videoCvDTO, @PathVariable Integer VideoCvId) {
        VideoCv videoCv = videoCvService. getVideoCv(VideoCvId);
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(videoCvDTO.getJob_seeker_id());
        if (jobSeeker == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videoCvService.updateVideoCv(videoCv,jobSeeker,
                videoCvDTO.getVideo_message(),videoCvDTO.getName()),
                HttpStatus.OK);
    }

}

