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
    @Autowired
    UserService userService;

    @GetMapping ("/employer/showall")
    public ResponseEntity <List<Employer>> showall () {
        List<Employer> employers = employerService.getEmployers();
        return new ResponseEntity<>(employers, HttpStatus.OK);
    }



   @GetMapping ("/employer/{employerId}")
  public ResponseEntity <Employer> getEmployerById (@PathVariable Integer employerId) {
       return new ResponseEntity<>(employerService.getEmployer(employerId), HttpStatus.OK);

  }

   @PostMapping("/employer")
   public ResponseEntity <Employer> addEmployer (@RequestBody EmployerDTO employerDTO) {
        User user = userService.getUser(employerDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employerService.addEmployer(user, employerDTO.getInn(), employerDTO.getOgrn(),
               employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription()), HttpStatus.OK);
   }
   @PutMapping ("/employer/{employerId}")
   public ResponseEntity <Employer> updateEmployer (@RequestBody EmployerDTO employerDTO, @PathVariable Integer employerId) {
       Employer employer = employerService. getEmployer(employerId);
       User user = userService.getUser(employerDTO.getUser_id());
       if (employer == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<>(employerService.updateEmployer(employer, user, employerDTO.getInn(), employerDTO.getOgrn(),
              employerDTO.getName(), employerDTO.getLogo(), employerDTO.getDescription()),
                HttpStatus.OK);
  }
}


