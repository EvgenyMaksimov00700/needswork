package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.CurrentStateDTO;
import com.wanted.needswork.DTO.request.StateSendRequestDTO;
import com.wanted.needswork.DTO.request.ViewVacancyDTO;
import com.wanted.needswork.DTO.response.CurrentStateResponseDTO;
import com.wanted.needswork.DTO.response.ViewVacancyResponseDTO;
import com.wanted.needswork.models.*;
import com.wanted.needswork.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ViewVacancyController {
    @Autowired
    ViewVacancyService viewVacancyService;
    @Autowired
    UserService userService;
    @Autowired
    VacancyService vacancyService;
    @Autowired
    VideoCvService videoCvService;
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    HHService hhService;


    @GetMapping("/view/show")
    public ResponseEntity<List<ViewVacancyResponseDTO>> getViewVacancy(
            @RequestParam(name = "limit", required = false) Integer      limit,
            @RequestParam(name = "userId", required = false) BigInteger   userId,
            @RequestParam(name = "vacancyId", required = false) BigInteger vacancyId
    ) {
        List<ViewVacancy> viewVacancies = viewVacancyService.getViewVacancies(limit, userId, vacancyId);
        List<ViewVacancyResponseDTO> viewVacancyResponseDTOs = viewVacancies.stream()
                .map(viewVacancy -> viewVacancy.toResponseDTO(vacancyService.getVacancy(viewVacancy.getVacancyId())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(viewVacancyResponseDTOs, HttpStatus.OK);
    }


    @PostMapping("/view/create")
    public ResponseEntity<ViewVacancy> addViewVacancy (@RequestBody ViewVacancyDTO viewVacancyDTO) {
        List<ViewVacancy> viewVacancies = viewVacancyService
                .getForLastSeconds(300, viewVacancyDTO.getUserId(), viewVacancyDTO.getVacancyId());
        if (!viewVacancies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUser(viewVacancyDTO.getUserId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(viewVacancyService.addViewVacancy(user,viewVacancyDTO.getVacancyId()), HttpStatus.CREATED);
    }


}

