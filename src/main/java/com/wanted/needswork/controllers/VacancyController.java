package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.VacancyDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.IndustryService;
import com.wanted.needswork.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class VacancyController {
    @Autowired
    VacancyService vacancyService;
    @Autowired
    IndustryService industryService;
    @Autowired
    EmployerService employerService;

    @GetMapping("/vacancy/showall")
    public ResponseEntity<List<VacancyResponseDTO>> showall() {
        List<Vacancy> vacancies = vacancyService.getVacancy();
        List<VacancyResponseDTO> vacancyResponseDTOs = new java.util.ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            vacancyResponseDTOs.add(vacancy.toResponseDTO());
        }
        return new ResponseEntity<>(vacancyResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/vacancy/{vacancyId}")
    public ResponseEntity<VacancyResponseDTO> getVacancyByID(@PathVariable Integer vacancyId) {
        return new ResponseEntity<>(vacancyService.getVacancy(vacancyId).toResponseDTO(), HttpStatus.OK);

    }

    @PostMapping("/vacancy")
    public ResponseEntity<Vacancy> addVacancy(@RequestBody VacancyDTO vacancyDTO) {
        Employer employer = employerService.getEmployer(vacancyDTO.getEmployer_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Industry industry = industryService.getIndustry(vacancyDTO.getIndustry_id());
        if (industry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vacancyService.addVacancy(employer, industry, vacancyDTO.getPosition(),
                vacancyDTO.getCity(), vacancyDTO.getFromSalary(), vacancyDTO.getToSalary(), vacancyDTO.getWorkSchedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(), vacancyDTO.getExp(), vacancyDTO.getResponsibility()), HttpStatus.OK);
    }


    @PutMapping("/vacancy/{vacancyId}")
    public ResponseEntity<Vacancy> updateVacancy(@RequestBody VacancyDTO vacancyDTO, @PathVariable Integer vacancyId) {
        Vacancy vacancy = vacancyService.getVacancy(vacancyId);
        if (vacancy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Employer employer = employerService.getEmployer(vacancyDTO.getEmployer_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Industry industry = industryService.getIndustry(vacancyDTO.getIndustry_id());
        return new ResponseEntity<>(vacancyService.updateVacancy(vacancy, employer, industry, vacancyDTO.getPosition(),
                vacancyDTO.getCity(), vacancyDTO.getFromSalary(), vacancyDTO.getToSalary(), vacancyDTO.getWorkSchedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(), vacancyDTO.getExp(), vacancyDTO.getResponsibility()),
                HttpStatus.OK);
    }

    @GetMapping("/vacancy/city")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = vacancyService.getCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);

    }

    @GetMapping("/vacancy/user/{userId}")
    public ResponseEntity<List<VacancyResponseDTO>> getVacancyByUser(@PathVariable Integer userId) {
        List<Vacancy> vacancyUser = vacancyService.getVacancyUser(userId);
        return new ResponseEntity<>(vacancyUser.stream().map(Vacancy::toResponseDTO).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/vacancy/{vacancyId}")
    public ResponseEntity<VacancyResponseDTO> deleteVacancyByID(@PathVariable Integer vacancyId) {
        vacancyService.deleteVacancy(vacancyId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/vacancy/filter")
    public ResponseEntity<List<VacancyResponseDTO>> filter(@RequestParam String city, @RequestParam Integer industry) {
        List<Vacancy> vacancies = vacancyService.getVacancy();
        List<VacancyResponseDTO> vacancyResponseDTOs = new java.util.ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            if (Objects.equals(vacancy.getCity(), city) && Objects.equals(vacancy.getIndustry().getId(), industry)) {
                vacancyResponseDTOs.add(vacancy.toResponseDTO());
            }
        }
        return new ResponseEntity<>(vacancyResponseDTOs, HttpStatus.OK);
    }

}

