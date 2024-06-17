package com.wanted.needswork.services;


import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class IndustryService {
    @Autowired
    EmployerRepository industryRepository;
    public List <User> getIndustry () {
        return industryRepository.findAll();
    }
}