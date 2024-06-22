package com.wanted.needswork.services;


import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.JobSeeker;
//import com.wanted.needswork.repository.JobSeeker;
import com.wanted.needswork.repository.IndustryRepository;
import com.wanted.needswork.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class IndustryService {
    @Autowired
    static
    IndustryRepository industryRepository;
    public List<Industry> getIndustryes() {
        return industryRepository.findAll();
    }

    public Industry getIndustry(Integer industryId) {
        return industryRepository.findById(industryId).orElse(null);
    }

//    public Industry addIndustry(String industryName) {
//        Industry industry = new Industry(industryName);
//        return industryRepository.save(industry);
//    }
}
