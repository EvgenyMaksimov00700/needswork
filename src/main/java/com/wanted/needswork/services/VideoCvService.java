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

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class VideoCvService {
    @Autowired
    VideoCvRepository videoCvRepository;

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

    public void sendVideoNote(BigInteger chatId, String fileIdOrUrl) {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format("{\"chat_id\":\"%d\", \"video_note\":\"%s\"}", chatId, fileIdOrUrl);
        Dotenv dotenv=Dotenv.load();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot"+dotenv.get("TOKEN")+"/sendVideoNote"))
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
}