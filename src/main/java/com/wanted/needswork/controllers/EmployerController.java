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

import java.util.List;

@RestController
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @Autowired
    UserService userService;

    @GetMapping ("/employer/showall")
    public ResponseEntity <List<EmployerResponseDTO>> showall () {
        List<Employer> employers = employerService.getEmployers();
        List<EmployerResponseDTO> employerResponseDTOs = new java.util.ArrayList<>();
        for (Employer employer : employers) {
            employerResponseDTOs.add(employer.toResponseDTO());
        }
        return new ResponseEntity<>(employerResponseDTOs, HttpStatus.OK);
    }



   @GetMapping ("/employer/{employerId}")
  public ResponseEntity <EmployerResponseDTO> getEmployerById (@PathVariable Integer employerId) {
       return new ResponseEntity<>(employerService.getEmployer(employerId).toResponseDTO(), HttpStatus.OK);

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


