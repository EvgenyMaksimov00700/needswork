package com.wanted.needswork.services;

import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.repository.JobSeekerRepository;
import com.wanted.needswork.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ResponseService {
    @Autowired
    static
    ResponseRepository responseRepository;
    public static List<Response> getResponse() {
        return responseRepository.findAll();
    }
}