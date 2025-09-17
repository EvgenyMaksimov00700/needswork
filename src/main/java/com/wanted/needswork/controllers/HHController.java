package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.EmailRequestDTO;
import com.wanted.needswork.DTO.request.HHApplyRequest;
import com.wanted.needswork.services.HHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HHController {
    @Autowired
    HHService hhService;

    @PostMapping ("/api/email/send")
    public ResponseEntity<Object> sendEmail (@RequestBody EmailRequestDTO emailDTO){
        String url = "https://t.me/tworker_ru_bot?start=response_" + emailDTO.getResponseID();
        String vacancyName = emailDTO.getVacancyName();
        hhService.sendResponseEmail(emailDTO.getEmail(), url, vacancyName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/city")
    public ResponseEntity<Object> getCity() {
        return new ResponseEntity<>(hhService.fetchCities(),
                HttpStatus.OK);
    }
    @GetMapping("/api/employer")
    public ResponseEntity<Object> getEmployer() throws Exception {
        return new ResponseEntity<>(hhService.fetchEmployers(),
                HttpStatus.OK);
    }
    @PostMapping ("/api/auth")
    public ResponseEntity<String> apiAuth (@RequestBody String code){
        return new ResponseEntity<>(hhService.updateToken(code), HttpStatus.OK);
    }
    @PostMapping("/api/hh/respond-and-message")
    public ResponseEntity<?> respondAndMessage(@RequestBody HHApplyRequest dto) {
        var result = hhService.respondAndMessage(dto.getVacancyId(), dto.getResponseId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
