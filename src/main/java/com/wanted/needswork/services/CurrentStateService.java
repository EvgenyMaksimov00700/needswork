package com.wanted.needswork.services;


import com.wanted.needswork.models.*;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.repository.CurrentStateRepository;
import com.wanted.needswork.repository.EmployerRepository;
import com.wanted.needswork.repository.IndustryRepository;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class CurrentStateService {
    @Autowired
    CurrentStateRepository currentStateRepository;

    public CurrentState getCurrentState(Integer currentStateId) {
        return currentStateRepository.findById(currentStateId).orElse(null);
    }
    public CurrentState getCurrentStateByUser(BigInteger userId) {
        return currentStateRepository.findByUserId(userId);
    }

    public CurrentState addCurrentState(User user, BigInteger vacancyId, String urlParams) {
        CurrentState currentState = new CurrentState(user, vacancyId, urlParams);
        return currentStateRepository.save(currentState);
    }

    public CurrentState deleteCurrentState(CurrentState currentState) {
        currentStateRepository.delete(currentState);
        return currentState;
    }

}

