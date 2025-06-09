package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.CurrentStateDTO;
import com.wanted.needswork.DTO.response.CurrentStateResponseDTO;
import com.wanted.needswork.models.CurrentState;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class CurrentStateController {
    @Autowired
    CurrentStateService currentStateService;
    @Autowired
    UserService userService;
    @Autowired
    VacancyService vacancyService;
    @Autowired
    HHService hhService;

    @GetMapping("/state/user/{userId}")
    public ResponseEntity<CurrentStateResponseDTO> getCurrentState(@PathVariable BigInteger userId) {
        CurrentState currentState = currentStateService.getCurrentStateByUser(userId);
        if (currentState == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BigInteger vacancyId = currentState.getVacancyId();
        Vacancy vacancy = vacancyService.getVacancy(vacancyId);
        if (vacancy==null) {
            vacancy = hhService.fetchVacancy(vacancyId);
        }
        return new ResponseEntity<>(currentState.toResponseDTO(vacancy), HttpStatus.OK);

    }

    @PostMapping("/state/create")
    public ResponseEntity<CurrentState> addCurrentState (@RequestBody CurrentStateDTO currentStateDTO) {
        User user = userService.getUser(currentStateDTO.getUserId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currentStateService.addCurrentState(user,currentStateDTO.getVacancyId(), currentStateDTO.getUrlParams()), HttpStatus.CREATED);
    }


    @DeleteMapping("/state/{stateId}")
    public ResponseEntity<CurrentState> deleteCurrentState(@PathVariable Integer stateId) {
        CurrentState currentState = currentStateService.getCurrentState(stateId);
        if (currentState == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currentStateService.deleteCurrentState(currentState),
                HttpStatus.OK);
    }

}


