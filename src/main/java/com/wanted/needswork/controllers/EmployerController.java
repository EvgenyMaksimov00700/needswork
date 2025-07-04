package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmployerDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @Autowired
    UserService userService;

    @GetMapping("/employer/showall")
    public ResponseEntity<List<EmployerResponseDTO>> showall() {
        List<Employer> employers = employerService.getEmployers();
        List<EmployerResponseDTO> employerResponseDTOs = new java.util.ArrayList<>();
        for (Employer employer : employers) {
            employerResponseDTOs.add(employer.toResponseDTO());
        }
        return new ResponseEntity<>(employerResponseDTOs, HttpStatus.OK);
    }


    @GetMapping("/employer/get/{employerId}")
    public ResponseEntity<EmployerResponseDTO> getEmployerById(@PathVariable BigInteger employerId) {
        Employer employer = employerService.getEmployer(employerId);
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employer.toResponseDTO(), HttpStatus.OK);

    }

    @GetMapping("/employer/user/{userId}")
    public ResponseEntity<EmployerResponseDTO> getEmployerByUserId(@PathVariable BigInteger userId) {
        Employer employer = employerService.getEmployerByUser(userId);
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employer.toResponseDTO(), HttpStatus.OK);

    }

    @PostMapping("/employer/create")
    public ResponseEntity<Employer> addEmployer(@RequestBody EmployerDTO employerDTO) {
        User user = userService.getUser(employerDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employerService.addEmployer(user, employerDTO.getInn(), employerDTO.getOgrn(),
                employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription(), employerDTO.getEmail(), employerDTO.getPhone()),  HttpStatus.OK);
    }

    @PutMapping("/employer/{employerId}")
    public ResponseEntity<Employer> updateEmployer(@RequestBody EmployerDTO employerDTO, @PathVariable BigInteger employerId) {
        Employer employer = employerService.getEmployer(employerId);
        User user = userService.getUser(employerDTO.getUser_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employerService.updateEmployer(employer, user, employerDTO.getInn(), employerDTO.getOgrn(),
                employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription(), employerDTO.getEmail(), employerDTO.getPhone()),
                HttpStatus.OK);
    }
    @DeleteMapping("/employer/{employerId}")
    public ResponseEntity<Employer> deleteEmployer(@PathVariable BigInteger employerId) {
        Employer employer = employerService.getEmployer(employerId);
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employerService.deleteEmployer(employer),
                HttpStatus.OK);
    }
    @GetMapping("/employer/user/inn/{inn}")
    public ResponseEntity<EmployerResponseDTO> getEmployerByInn(@PathVariable BigInteger inn) {
        Employer employer = employerService.getEmployerByInn(inn);
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employer.toResponseDTO(), HttpStatus.OK);

    }
    @GetMapping("/employer/user/email")
    public ResponseEntity<EmployerResponseDTO> getEmployerByEmail(@RequestParam String email) {
        Employer employer = employerService.getEmployerByEmail(email);
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(employer.toResponseDTO(), HttpStatus.OK);

    }
}


