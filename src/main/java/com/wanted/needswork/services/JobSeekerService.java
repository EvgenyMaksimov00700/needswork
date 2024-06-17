package com.wanted.needswork.services;


import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class JobSeekerService {
    @Autowired
    static
    JobSeekerRepository jobSeekerRepository;
    public static List<JobSeeker> getJobSeekers() {
        return jobSeekerRepository.findAll();
    }
}
