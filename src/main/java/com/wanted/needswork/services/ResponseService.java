package com.wanted.needswork.services;

import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.JobSeekerRepository;
import com.wanted.needswork.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class ResponseService {
    @Autowired
    ResponseRepository responseRepository;
    public List<Response> getResponse() {
        return responseRepository.findAll();
    }

    public Response getResponse(Integer id) {
        return responseRepository.findById(id).orElse(null);
    }

    public Response addResponse(Vacancy vacancy_id, JobSeeker job_seeker_id, String comment, Integer date_time){
        Response response = new Response(vacancy_id, job_seeker_id, comment, date_time);
        return responseRepository.save(response);
    }

    public Response updateResponse(Response response, Vacancy vacancy_id, JobSeeker job_seeker_id, String comment,
                                   Integer date_time) {
        if (vacancy_id != null) {
            response.setVacancy(vacancy_id);
        }
        if (job_seeker_id!= null) {
            response.setJob_seeker(job_seeker_id);
        }
        if (comment!= null) {
        response.setComment(comment);
        }
        if (date_time!= null) {
            response.setDate_time(date_time);
        }
        return responseRepository.save(response);
    }
}
