package com.wanted.needswork.repository;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository <JobSeeker, Integer> {

    JobSeeker getJobSeekerByUserId(Integer userId);
}

