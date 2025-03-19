package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmailResponseDTO;
import com.wanted.needswork.services.HHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HHController {
    @Autowired
    HHService hhService;

    @PostMapping ("/api/email/send")
    public ResponseEntity<Object> sendEmail (@RequestBody EmailResponseDTO emailDTO){
        String url = "https://t.me/tworker_ru_bot?start=response_" + emailDTO.getResponseID();
        String vacancyName = emailDTO.getVacancyName();
        hhService.sendResponseEmail(emailDTO.getEmail(), url, vacancyName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/api/industry")
//    public ResponseEntity<Object> getIndustry() {
//        return new ResponseEntity<>(hhService.fetchIndustries(),
//                HttpStatus.OK);
//    }
//
//    @PostMapping("/api/industry")
//    public ResponseEntity<Object> addIndustry() {
//        List<List<String>> industries = hhService.fetchIndustries();
//        hhService.addIndustries(industries);
//        return new ResponseEntity<>(hhService.fetchIndustries(),
//                HttpStatus.OK);
//    }
}
