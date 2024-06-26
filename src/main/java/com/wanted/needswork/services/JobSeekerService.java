package com.wanted.needswork.services;


import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class JobSeekerService {
    @Autowired
    JobSeekerRepository jobSeekerRepository;

    public JobSeeker getJobSeeker(Integer id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }
    public List<JobSeeker> getJobSeekers() {
        return jobSeekerRepository.findAll();

    }
    public JobSeeker addJobSeeker(User user, String video_cv, Double latitude, Double longitude){
        JobSeeker jobSeeker = new JobSeeker (user, video_cv, latitude, longitude);
        return jobSeekerRepository.save(jobSeeker);
    }

    public JobSeeker updateJobSeeker(JobSeeker jobSeeker,User user, String video_cv, Double latitude, Double longitude) {
        if (latitude != null) {
            jobSeeker.setLatitude(latitude);
        }
        if (longitude != null){
            jobSeeker.setLongitude(longitude);
        }
        if (user != null){
            jobSeeker.setUser(user);
        }
        if (video_cv != null) {
            jobSeeker.setVideo_cv(video_cv);
        }
        return jobSeekerRepository.save(jobSeeker);
    }

}
