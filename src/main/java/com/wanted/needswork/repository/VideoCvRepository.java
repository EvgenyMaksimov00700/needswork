package com.wanted.needswork.repository;

import com.wanted.needswork.models.JobSeeker;
import com.wanted.needswork.models.VideoCv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoCvRepository  extends JpaRepository<VideoCv, Integer> {

    List<VideoCv> findAllByJobSeeker(JobSeeker jobSeeker);
}
