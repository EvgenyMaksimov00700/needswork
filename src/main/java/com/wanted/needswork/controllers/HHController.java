package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.IndustryDTO;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.services.HHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HHController {
    @Autowired
    HHService hhService;

    @GetMapping("/api/industry")
    public ResponseEntity<Object> addIndustry() {
        return new ResponseEntity<>(hhService.fetchIndustries(),
                HttpStatus.OK);
    }
}
