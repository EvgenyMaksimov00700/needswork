package com.wanted.needswork.services;

import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.JobSeekerRepository;
import com.wanted.needswork.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class ResponseService {
    @Autowired
    static
    ResponseRepository responseRepository;
    public List<Response> getResponse() {
        return responseRepository.findAll();
    }

    public Response getResponse(Integer id) {
        return responseRepository.findById(id).orElse(null);
    }

    public Response addResponse(Integer vacancy_id, Integer job_seeker_id, String comment, Integer date_time){
        Response response = new Response(vacancy_id, job_seeker_id, comment, date_time);
        return responseRepository.save(response);
    }

    public Response updateResponse(Response response, Integer vacancy_id, Integer job_seeker_id, String comment,
                                   Integer date_time) {
        response.setVacancy_id(vacancy_id);
        response.setJob_seeker_id(job_seeker_id);
        response.setComment(comment);
        response.setDate_time(date_time);
        return responseRepository.save(response);
    }
}
