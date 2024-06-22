package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmployerDTO;
import com.wanted.needswork.DTO.request.IndustryDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @GetMapping ("/employer/showall")
    public ResponseEntity <List<Employer>> showall () {
        return new ResponseEntity<>(employerService.getEmployers(), HttpStatus.OK);
    }

//    @GetMapping ("/employer/{employerId}")
//    public ResponseEntity <Industry> getEmployerByID (@PathVariable Integer employerId) {
//        return new ResponseEntity<>(Employer.getEmployer(EmployerId), HttpStatus.OK);
//
//    }
//
//    @PostMapping("/employer")
//    public ResponseEntity <Industry> addemployer (@RequestBody EmployerDTO employerDTO) {
//        return new ResponseEntity<>(employerService.addEmployer(employerDTO.getUser_id(), employerDTO.getInn(), employerDTO.getOgrn(),
//                employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription()),
//                HttpStatus.OK);
//    }
//    @PutMapping ("/employer")
//    public ResponseEntity <Employer> updateEmployer (@RequestBody EmployerDTO employerDTO) {
//        Employer employer = employerService. getEmployers(employerId);
//        if (employer == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(employerService.updateEmployer(employer, employerDTO.getUser_id(), employerDTO.getInn(), employerDTO.getOgrn(),
//                employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription()),
//                HttpStatus.OK);
//    }
}


