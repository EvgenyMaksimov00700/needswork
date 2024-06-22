package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.DTO.request.VacancyDTO;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.services.IndustryService;
import com.wanted.needswork.services.UserService;
import com.wanted.needswork.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VacancyController {
    @Autowired
    VacancyService vacancyService;
    @GetMapping ("/vacancy/showall")
    public ResponseEntity <List<Vacancy>> showall () {
        return new ResponseEntity<>(vacancyService.getVacancy(), HttpStatus.OK);
    }
    @PostMapping("/vacancy")
    public ResponseEntity <Vacancy> addVacancy (@RequestBody VacancyDTO vacancyDTO) {
        return new ResponseEntity<>(vacancyService.addVacancy(vacancyDTO.getEmployer_id(), vacancyDTO.getIndustry_id(), vacancyDTO.getPosition(),
                vacancyDTO.getCity(), vacancyDTO.getSalary(), vacancyDTO.getWorkShedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(),vacancyDTO.getDate_Time()),HttpStatus.OK);
    }
    @PutMapping("/vacancy/{vacancyId}")
    public ResponseEntity <Vacancy> updateVacancy (@RequestBody VacancyDTO vacancyDTO, @PathVariable Integer vacancyId) {
        Vacancy vacancy = vacancyService. getVacancy(vacancyId);
        if (vacancy == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vacancyService.addVacancy(vacancyDTO.getEmployer_id(), vacancyDTO.getIndustry_id(), vacancyDTO.getPosition(),
                vacancyDTO.getCity(), vacancyDTO.getSalary(), vacancyDTO.getWorkShedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(),vacancyDTO.getDate_Time()),
                HttpStatus.OK);
    }
}

