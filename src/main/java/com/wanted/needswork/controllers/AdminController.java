package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmployerDTO;
import com.wanted.needswork.DTO.response.AdminTotalInfoResponseDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    EmployerService employerService;
    @Autowired
    UserService userService;
    @Autowired
    VacancyService vacancyService;
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    ResponseService responseService;

    @GetMapping("/admin/info/total")
    public ResponseEntity<AdminTotalInfoResponseDTO> info() {
        Integer totalEmployers = employerService.getEmployers().size();
        Integer totalVacancies = vacancyService.getVacancy().size();
        Integer totalJobSeekers = jobSeekerService.getJobSeekers().size();
        Integer totalResponses = responseService.getResponse().size();
        Integer totalVideoResponses = responseService.getVideoResponses().size();
        Integer totalTextResponses = responseService.getTextResponses().size();
        Integer totalWithoutResponses = responseService.getWithoutResume().size();
        Integer totalPayed = 0;
        Integer totalAvgTime = 0;
        AdminTotalInfoResponseDTO responseDTO = new AdminTotalInfoResponseDTO(
                totalVacancies,
                totalResponses,
                totalVideoResponses,
                totalTextResponses,
                totalWithoutResponses,
                totalPayed,
                totalJobSeekers,
                totalEmployers,
                totalAvgTime
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/info/employer/{userId}")
    public ResponseEntity<AdminTotalInfoResponseDTO> info(@PathVariable BigInteger userId) {
    Integer totalAmountVacancies = vacancyService.getVacancyUser(userId).size();
    //Integer totalAmountResponses = responseService.getResponseUser(userId).size();
    List<Vacancy> vacancies= vacancyService.getVacancyUser(userId);
    Integer totalPayed = 0;
    return null;
    }
}




