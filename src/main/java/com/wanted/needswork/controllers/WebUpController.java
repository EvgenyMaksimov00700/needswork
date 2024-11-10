package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.TgMessageDTO;
import com.wanted.needswork.DTO.request.VideoCvSendDTO;
import com.wanted.needswork.models.VideoCv;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class WebUpController {
    @GetMapping("/employer/reg/")
    public String getEmployerReg() {
        return "reg_employer";

    }

    @GetMapping("/employer/lk/")
    public String getEmployerLk() {
        return "lk_employer";
    }

    @GetMapping("/employer/vacancy/create")
    public String getCreateVacancyForm() {
        return "create_vacancy_form";
    }

    @GetMapping("/employer/my_vacancy7/show")
    public String getMyVacancy7() {
        return "my_vacancy7";
    }

    @GetMapping("/employer/reg_employer1/reg")
    public String getRegEmployer1() {
        return "reg_employer1";
    }

    @GetMapping("/employer/responses7/show")
    public String getResponses7() {
        return "responses7";
    }

    @GetMapping("/employer/vacancy/description")
    public String getDescription() {
        return "description_vacancy";
    }

    @GetMapping("/employer/vacancy/edit")
    public String editVacancy() {
        return "create_vacancy_edit";
    }

    @GetMapping("/vacancy/menu")
    public String menuVacancy() {
        return "menuClient";
    }

    @GetMapping("/vacancy/description")
    public String descriptionVacancy() {
        return "description_vacancy_job_seeker";
    }

    @GetMapping("/jobSeeker/account/favorites")
    public String favoritesVacancy() {
        return "favorites_vacancy";
    }

    @GetMapping("/jobSeeker/account/menu")
    public String jobSeekersMenu() {
        return "job_seekers_menu";
    }

    @PostMapping("/message/send")
    public ResponseEntity<Object> messageSend (@RequestBody TgMessageDTO tgMessageDTO) {

        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format("{\"chat_id\":\"%d\", \"text\":\"%s\"}", tgMessageDTO.getUserId(), tgMessageDTO.getMessage());
        Dotenv dotenv=Dotenv.load();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot"+dotenv.get("TOKEN")+"/sendMessage"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ от Telegram: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
