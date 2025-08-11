package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmployerDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.FileStorageService;
import com.wanted.needswork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @Autowired
    UserService userService;
    @Autowired
    FileStorageService fileStorageService;
    @Value("${app.logo.base-path}")
    private String logoBasePath;

    @GetMapping("/employer/showall")
    public ResponseEntity<List<EmployerResponseDTO>> showall() {
        List<Employer> employers = employerService.getEmployers();
        List<EmployerResponseDTO> employerResponseDTOs = new java.util.ArrayList<>();
        for (Employer employer : employers) {
            employerResponseDTOs.add(employer.toResponseDTO());
        }
        return new ResponseEntity<>(employerResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/employer/logo/{employerId}")
    public ResponseEntity<Resource> getEmployerLogo(@PathVariable BigInteger employerId) throws MalformedURLException {
        String fileName = employerService.getEmployer(employerId).getLogo();
        Path file = Paths.get(logoBasePath).resolve(fileName);

        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toUri());

        String contentType;
        try {
            contentType = Files.probeContentType(file);
        } catch (IOException e) {
            contentType = "application/octet-stream"; // fallback
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
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

    @PostMapping(value="/employer/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Employer> addEmployer(@ModelAttribute EmployerDTO employerDTO) throws IOException {
        User user = userService.getUser(employerDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String logoFileName;
        if (employerDTO.getLogo() != null) {
        logoFileName= fileStorageService.storeLogo(employerDTO.getLogo());}
        else {
            logoFileName = null;
        }
        return new ResponseEntity<>(employerService.addEmployer(user, employerDTO.getInn(), employerDTO.getOgrn(),
                employerDTO.getName(), logoFileName, employerDTO.getDescription(), employerDTO.getEmail(), employerDTO.getPhone()),  HttpStatus.OK);
    }

    @PutMapping(value = "/employer/{employerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateEmployer(
            @ModelAttribute EmployerDTO employerDTO,
            @PathVariable BigInteger employerId) throws IOException {
        System.out.println(employerId);
        Employer employer = employerService.getEmployer(employerId);
        System.out.println(employerDTO.getUser_id());
        User user = userService.getUser(employerDTO.getUser_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String logoFileName;
        if (employerDTO.getLogo() != null) {
            logoFileName= fileStorageService.storeLogo(employerDTO.getLogo());}
        else {
            logoFileName = null;
        }

        return new ResponseEntity<>(employerService.updateEmployer(employer, user, employerDTO.getInn(), employerDTO.getOgrn(),
                employerDTO.getName(), logoFileName, employerDTO.getDescription(), employerDTO.getEmail(), employerDTO.getPhone()),
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


