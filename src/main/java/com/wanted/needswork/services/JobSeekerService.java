package com.wanted.needswork.services;


import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class JobSeekerService {
    @Autowired
    static
    JobSeekerRepository jobSeekerRepository;

    public JobSeeker getJobSeeker(Integer id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }
    public List<JobSeeker> getJobSeekers() {
        return jobSeekerRepository.findAll();

    }
    public JobSeeker addJobSeeker(Integer user_id, String video_cv, Double location){
        JobSeeker jobSeeker = new JobSeeker (user_id, video_cv, location);
        return jobSeekerRepository.save(jobSeeker);
    }

    public JobSeeker updateJobSeeker(JobSeeker jobSeeker,Integer user_id, String video_cv, Double location) {
        jobSeeker.setLocation(location);
        jobSeeker.setUser_id(user_id);
        jobSeeker.setVideo_cv(video_cv);
        return jobSeekerRepository.save(jobSeeker);
    }

}
