package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.ResponseDTO;
import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.ResponseResponseDTO;
import com.wanted.needswork.models.*;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.ResponseService;
import com.wanted.needswork.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class ResponseController {
    @Autowired
    ResponseService responseService;
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    VacancyService vacancyService;
    @GetMapping ("/response/showall")
    public ResponseEntity <List<ResponseResponseDTO>> showall () {
        List<Response> responses = responseService.getResponse();
        List<ResponseResponseDTO> responseResponseDTOs = new java.util.ArrayList<>();
        for (Response response : responses) {
            responseResponseDTOs.add(response.toResponseDTO());
        }

        return new ResponseEntity<>(responseResponseDTOs, HttpStatus.OK);
    }
    @GetMapping ("/response/{responseId}")
    public ResponseEntity <ResponseResponseDTO> getResponseByID (@PathVariable Integer responseId) {
        return new ResponseEntity<>(responseService.getResponse(responseId).toResponseDTO(), HttpStatus.OK);
    }
    @PostMapping("/response")
    public ResponseEntity <Response> addUser (@RequestBody ResponseDTO responseDTO) {
        Vacancy vacancy = vacancyService.getVacancy(responseDTO.getVacancy_id());
        if (vacancy == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(responseDTO.getJob_seeker_id());
        if (jobSeeker == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseService.addResponse(vacancy, jobSeeker, responseDTO.getComment(), responseDTO.getDate_time()),
                HttpStatus.OK);
    }
    @PutMapping ("/response/{responseId}")
    public ResponseEntity <Response> updateResponse (@RequestBody ResponseDTO responseDTO, @PathVariable Integer responseId) {
        Response response = responseService. getResponse(responseId);
        if (response == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Vacancy vacancy = vacancyService.getVacancy(responseDTO.getVacancy_id());
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(responseDTO.getJob_seeker_id());
        return new ResponseEntity<>(responseService.updateResponse(response,vacancy,jobSeeker, responseDTO.getComment(), responseDTO.getDate_time()),
                HttpStatus.OK);
    }
}
