package com.wanted.needswork.repository;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface JobSeekerRepository extends JpaRepository <JobSeeker, Integer> {

    JobSeeker getJobSeekerByUserId(BigInteger userId);
}

