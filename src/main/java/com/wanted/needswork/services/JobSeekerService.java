package com.wanted.needswork.services;


import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class JobSeekerService {
    @Autowired
    JobSeekerRepository jobSeekerRepository;

    public JobSeeker getJobSeeker(Integer id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }
    public List<JobSeeker> getJobSeekers() {
        return jobSeekerRepository.findAll().stream()
                .sorted((j1, j2) -> j2.getId().compareTo(j1.getId())).collect(Collectors.toList());

    }
    public JobSeeker addJobSeeker(User user, Double latitude, Double longitude){
        JobSeeker jobSeeker = new JobSeeker (user, latitude, longitude);
        return jobSeekerRepository.save(jobSeeker);
    }

    public JobSeeker updateJobSeeker(JobSeeker jobSeeker,User user, Double latitude, Double longitude) {
        if (latitude != null) {
            jobSeeker.setLatitude(latitude);
        }
        if (longitude != null){
            jobSeeker.setLongitude(longitude);
        }
        if (user != null){
            jobSeeker.setUser(user);
        }

        return jobSeekerRepository.save(jobSeeker);
    }

    public JobSeeker getJobSeekerByUserId(BigInteger userId) {
        return jobSeekerRepository.getJobSeekerByUserId(userId);
    }

    public void addTextResume(BigInteger jobSeekerId, String absolutePath) {
        JobSeeker jobSeeker = getJobSeekerByUserId(jobSeekerId);
        if (jobSeeker != null) {
            jobSeeker.setTextResume(absolutePath);
            jobSeekerRepository.save(jobSeeker);
        }
    }

    public String getTextResumeFileName(Integer jobSeekerId) {
        JobSeeker entity = jobSeekerRepository.findById(jobSeekerId)
                .orElse(null);

        assert entity != null;
        return entity.getTextResume();
    }
}
