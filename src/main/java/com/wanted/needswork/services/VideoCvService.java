package com.wanted.needswork.services;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.models.VideoCv;
import com.wanted.needswork.repository.JobSeekerRepository;
import com.wanted.needswork.repository.VideoCvRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import kong.unirest.MultipartBody;
import kong.unirest.Unirest;

@Service
public class VideoCvService {
    @Autowired
    VideoCvRepository videoCvRepository;
    @Autowired
    JobSeekerRepository jobSeekerRepository;
    public VideoCv getVideoCv(Integer id) {
        return videoCvRepository.findById(id).orElse(null);
    }

    public List<VideoCv> getVideoCv() {
        return videoCvRepository.findAll();

    }

    public VideoCv addVideoCv(JobSeeker jobSeeker, String video_message, String name) {
        VideoCv videoCv = new VideoCv(jobSeeker, video_message, name);
        return videoCvRepository.save(videoCv);
    }

    public VideoCv updateVideoCv(VideoCv videoCv, JobSeeker jobSeeker, String video_message, String name) {
        if (jobSeeker != null) {
            videoCv.setJobSeeker(jobSeeker);
        }
        if (video_message != null) {
            videoCv.setVideo_message(video_message);
        }
        if (name != null) {
            videoCv.setName(name);
        }

        return videoCvRepository.save(videoCv);
    }

    public List<VideoCv> getVideoCvByUser(JobSeeker jobSeeker) {

        return videoCvRepository.findAllByJobSeeker(jobSeeker);
    }


    public void deleteVideoCv(Integer videoCvId) {
        Optional<VideoCv> videoCv = videoCvRepository.findById(videoCvId);
        if (videoCv.isPresent()) {
            videoCvRepository.delete(videoCv.get());
        } else {
            System.out.println("VideoCv not found with id: " + videoCvId);
        }
    }

    public void sendVideoNote(User user, String fileIdOrUrl, Vacancy vacancy, Boolean textResume) {

        HttpClient client2 = HttpClient.newHttpClient();
        Dotenv dotenv = Dotenv.load();
        if (fileIdOrUrl != null) {
            HttpClient client = HttpClient.newHttpClient();
            User employer_user = user;
            if (vacancy != null) {
                employer_user = vacancy.getEmployer().getUser();
            }
            String requestBody = String.format("{\"chat_id\":\"%d\", \"video_note\":\"%s\"}", employer_user.getId(), fileIdOrUrl);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.telegram.org/bot" + dotenv.get("TOKEN") + "/sendVideoNote"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Ответ от Telegram: " + response.body());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (vacancy != null) {
            User employer_user = vacancy.getEmployer().getUser();

            // Формируем ссылку для ФИО
            String fioLink;
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                fioLink = String.format("<a href='https://t.me/%s'>%s</a>", user.getUsername(), user.getFullName());
            } else {
                fioLink = String.format("<a href='tg://user?id=%d'>%s</a>", user.getId(), user.getFullName());
            }

            // Формируем номер телефона с + если его нет
            String phone = user.getPhone();
            if (phone != null && !phone.isEmpty() && !phone.startsWith("+")) {
                phone = "+" + phone;
            }

            String textMessage = String.format(
                    "Отклик по вакансии <b>%s</b>\nФИО: %s\nтел: %s",
                    vacancy.getPosition(), fioLink, phone != null ? phone : "не указан"
            );


            if (textResume) {
            JobSeeker jobSeeker = jobSeekerRepository.getJobSeekerByUserId(user.getId());
                String url = "https://api.telegram.org/bot" + dotenv.get("TOKEN") + "/sendDocument";

                kong.unirest.HttpResponse <String> response = Unirest.post(url)
                        .field("chat_id", employer_user.getId())
                        .field("caption", textMessage)  // Можно добавить подпись
                        .field("document", new File(jobSeeker.getTextResume())) // Загружаем файл
                        .field("parse_mode", "HTML")
                        .asString();

                System.out.println(response.getBody());
            } else {
                String requestBody2 = String.format(
                        "{\"chat_id\":\"%d\", \"text\":\"%s\", \"parse_mode\":\"HTML\"}",
                        employer_user.getId(), textMessage
                );
                System.out.println(requestBody2);
                System.out.println("https://api.telegram.org/bot" + dotenv.get("TOKEN") + "/sendMessage");
                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.telegram.org/bot" + dotenv.get("TOKEN") + "/sendMessage"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody2))
                        .build();
                try {

                    HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Ответ от Telegram: " + response2.body());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}