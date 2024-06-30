package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.IndustryDTO;
import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
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

import java.math.BigInteger;
import java.util.List;

@RestController
public class IndustryController {
    @Autowired
    IndustryService industryService;
    @GetMapping ("/industry/showall")
    public ResponseEntity <List<IndustryResponseDTO>> showall () {
        List<Industry> industryes= industryService.getIndustryes();
        List<IndustryResponseDTO> industryResponseDTOs = new java.util.ArrayList<>();
        for (Industry industry : industryes) {
            industryResponseDTOs.add(industry.toResponseDTO());
        }
        return new ResponseEntity<>(industryResponseDTOs, HttpStatus.OK);
    }



    @GetMapping ("/industry/{industryId}")
    public ResponseEntity <IndustryResponseDTO> getIndustryById (@PathVariable Integer industryId) {
        return new ResponseEntity<>(industryService.getIndustry(industryId).toResponseDTO(), HttpStatus.OK);

    }

    @PostMapping("/industry")
    public ResponseEntity <Industry> addIndustry (@RequestBody IndustryDTO industryDTO) {
        return new ResponseEntity<>(industryService.addIndustry(industryDTO.getUsername(), industryDTO.getCategory()),
                HttpStatus.OK);
    }
    @PutMapping ("/industry/{industryId}")
    public ResponseEntity <Industry> updateIndustry (@RequestBody IndustryDTO industryDTO, @PathVariable Integer industryId) {
        Industry industry = industryService. getIndustry(industryId);
        if (industry == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(industryService.updateIndustry(industry, industryDTO.getUsername(), industryDTO.getCategory()),
                HttpStatus.OK);
    }
}