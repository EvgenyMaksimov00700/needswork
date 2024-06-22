package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.ResponseDTO;
import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.services.EmployerService;
import com.wanted.needswork.services.JobSeekerService;
import com.wanted.needswork.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class ResponseController {
    @Autowired
    ResponseService responseService;
    @GetMapping ("/Response/showall")
    public ResponseEntity <List<Response>> showall () {
        return new ResponseEntity<>(responseService.getResponse(), HttpStatus.OK);
    }
    @GetMapping ("/response/{responseId}")
    public ResponseEntity <Response> getResponseByID (@PathVariable Integer responseId) {
        return new ResponseEntity<>(responseService.getResponse(responseId), HttpStatus.OK);
    }
    @PostMapping("/response")
    public ResponseEntity <Response> addUser (@RequestBody ResponseDTO responseDTO) {
        return new ResponseEntity<>(responseService.addResponse(responseDTO.getVacancy_id(), responseDTO.getJob_seeker_id(), responseDTO.getComment(), responseDTO.getDate_time()),
                HttpStatus.OK);
    }
    @PutMapping ("/response/{responseId}")
    public ResponseEntity <Response> updateResponse (@RequestBody ResponseDTO responseDTO, @PathVariable Integer responseId) {
        Response response = responseService. getResponse(responseId);
        if (response == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseService.updateResponse(response, responseDTO.getVacancy_id(), responseDTO.getJob_seeker_id(), responseDTO.getComment(), responseDTO.getDate_time()),
                HttpStatus.OK);
    }
}
