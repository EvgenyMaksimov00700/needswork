package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.ResponseDTO;
import com.wanted.needswork.DTO.request.UserDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.ResponseResponseDTO;
import com.wanted.needswork.DTO.response.ResponseVacancyUserDTO;
import com.wanted.needswork.models.*;
import com.wanted.needswork.services.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class ResponseController {
    @Autowired
    ResponseService responseService;
    @Autowired
    JobSeekerService jobSeekerService;
    @Autowired
    VacancyService vacancyService;
    @Autowired
    HHService hhService;
    @GetMapping ("/response/showall")
    public ResponseEntity <List<ResponseResponseDTO>> showall () {
        List<Response> responses = responseService.getResponse();
        List<ResponseResponseDTO> responseResponseDTOs = new java.util.ArrayList<>();
        for (Response response : responses) {
            responseResponseDTOs.add(response.toResponseDTO());
        }

        return new ResponseEntity<>(responseResponseDTOs, HttpStatus.OK);
    }
    @GetMapping ("/response/{responseId}")
    public ResponseEntity <ResponseResponseDTO> getResponseByID (@PathVariable Integer responseId) {
        return new ResponseEntity<>(responseService.getResponse(responseId).toResponseDTO(), HttpStatus.OK);
    }
    @PostMapping("/response")
    public ResponseEntity <Response> addUser (@RequestBody ResponseDTO responseDTO) {
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(responseDTO.getJob_seeker_id());
        if (jobSeeker == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Response response = responseService.addResponse(responseDTO.getVacancy_id(), jobSeeker, responseDTO.getComment());
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format(
                "{" +
                        "\"chat_id\":\"%s\"," +
                        "\"text\":\"%s\"," +
                        "\"parse_mode\":\"HTML\"," +
                        "\"reply_markup\":{" +
                        "\"inline_keyboard\":[[" +
                        "{\"text\":\"Открыть вакансию\",\"url\":\"https://tworker.ru/vacancy/description?id=%d\"}" +
                        "]]" +
                        "}" +
                        "}",
                "-1002705478587",
                "Пользователь <a href='tg://user?id=" + response.getJob_seeker().getUser().getId() + "'>" +
                        response.getJob_seeker().getUser().getFullName() + "</a> откликнулся на вакансию",
                response.getVacancyId()
        );

        Dotenv dotenv=Dotenv.load();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot"+dotenv.get("TOKEN")+"/sendMessage"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ от Telegram: " + resp.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,
                HttpStatus.OK);
    }
    @PutMapping ("/response/{responseId}")
    public ResponseEntity <Response> updateResponse (@RequestBody ResponseDTO responseDTO, @PathVariable Integer responseId) {
        Response response = responseService. getResponse(responseId);
        if (response == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JobSeeker jobSeeker = jobSeekerService.getJobSeeker(responseDTO.getJob_seeker_id());
        return new ResponseEntity<>(responseService.updateResponse(response,responseDTO.getVacancy_id(),jobSeeker, responseDTO.getComment()),
                HttpStatus.OK);
    }
    @GetMapping ("/response/vacancy/{vacancyId}")
    public ResponseEntity <List<ResponseResponseDTO>> getResponseByVacancyID (@PathVariable Integer vacancyId) {
        return new ResponseEntity<>(responseService.getResponsesByVacancyId(vacancyId).stream().map(Response::toResponseDTO).toList(), HttpStatus.OK);
    }

    @DeleteMapping ("/response/{responseId}")
    public ResponseEntity <ResponseResponseDTO> deleteResponseByID (@PathVariable Integer responseId) {
        return new ResponseEntity<>(responseService.deleteResponse(responseId).toResponseDTO(), HttpStatus.OK);
    }
    @GetMapping ("/response/contact/{responseId}")
    public ResponseEntity <ResponseVacancyUserDTO> getResponseContact (@PathVariable Integer responseId) {
        Response response = responseService.getResponse(responseId);
        User user = response.getJob_seeker().getUser();
        Vacancy vacancy = vacancyService.getVacancy(response.getVacancyId());
        if (vacancy==null) {
            vacancy = hhService.fetchVacancy(response.getVacancyId());
        }
        if (vacancy == null){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseVacancyUserDTO(vacancy.toResponseDTO(), user.toResponseDTO(), response.getComment()),  HttpStatus.OK);
    }
}
