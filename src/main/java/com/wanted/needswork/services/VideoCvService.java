package com.wanted.needswork.services;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.models.VideoCv;
import com.wanted.needswork.repository.JobSeekerRepository;
import com.wanted.needswork.repository.VideoCvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}