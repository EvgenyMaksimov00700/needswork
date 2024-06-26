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

    IndustryRepository industryRepository;
    public List<Industry> getIndustryes() {
        return industryRepository.findAll();
    }

    public Industry getIndustry(Integer industryId) {
        return industryRepository.findById(industryId).orElse(null);


    }

    public Industry addIndustry (String username, String category){
        Industry industry = new Industry (username, category);
        return industryRepository.save(industry);
    }

    public Industry updateIndustry (Industry industry, String username, String category) {
        if (username != null) {
            industry.setUsername(username);
        }
        if (category != null) {
            industry.setCategory(category);
        }
        return industryRepository.save(industry);
    }

}
