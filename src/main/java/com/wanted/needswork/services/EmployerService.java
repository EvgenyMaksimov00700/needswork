package com.wanted.needswork.services;


import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.EmployerRepository;
import com.wanted.needswork.repository.IndustryRepository;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class EmployerService {
    @Autowired
    EmployerRepository employerRepository;

    public List<Employer> getEmployers() {
        return employerRepository.findAll();
    }

    public Employer getEmployer(Integer employerId) {
        return employerRepository.findById(employerId).orElse(null);


    }
    public Employer getEmployerByUser(BigInteger userId) {
        return employerRepository.findByUserId(userId);


    }

    public Employer addEmployer(User user, BigInteger inn, BigInteger ogrn, String name, String logo, String description, String email) {
        Employer employer = new Employer(user, inn, ogrn, name, logo, description, email);
        return employerRepository.save(employer);
    }

    public Employer updateEmployer(Employer employer, User user, BigInteger inn, BigInteger ogrn, String name, String logo, String description, String email) {
        if (user != null) {
            employer.setUser(user);
        }
        if (inn != null) {
            employer.setInn(inn);
        }
        if (ogrn != null) {
            employer.setOgrn(ogrn);
        }
        if (name != null) {
            employer.setName(name);
        }
        if (logo != null) {
            employer.setLogo(logo);
        }
        if (description != null) {
            employer.setDescription(description);
        }
        if (email!= null) {
            employer.setEmail(email);
        }

        return employerRepository.save(employer);
    }

    public Employer deleteEmployer(Employer employer) {
        employerRepository.delete(employer);
        return employer;
    }

    public Employer getEmployerByInn(BigInteger inn) {
        return employerRepository.findByInn(inn);
    }
    public Employer getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email);
    }
}

