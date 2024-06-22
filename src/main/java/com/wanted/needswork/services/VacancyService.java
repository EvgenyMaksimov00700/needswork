package com.wanted.needswork.services;


import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service

public class VacancyService {
    @Autowired
    VacancyRepository vacancyRepository;

    public List<Vacancy> getVacancy() {
        return vacancyRepository.findAll();
    }

    public Vacancy getVacancy(Integer vacancyId) {
        return vacancyRepository.findById(vacancyId).orElse(null);
    }

    public Vacancy addVacancy( Integer employer_id, Integer industry_id, String position, String city, Integer salary,
    String workShedule, String distantWork, String address, Integer date_Time){
        Vacancy vacancy = new Vacancy(industry_id, employer_id, position, city, salary, workShedule, distantWork, address, date_Time);
        return vacancyRepository.save(vacancy);
    }

    public Vacancy updateVacancy(Vacancy vacancy, Integer employer_id, Integer industry_id, String position, String city,
                              Integer salary, String workShedule, String distantWork, String address, Integer date_Time) {
        vacancy.setIndustry_id(industry_id);
        vacancy.setEmployer_id(employer_id);
        vacancy.setPosition(position);
        vacancy.setCity(city);
        vacancy.setSalary(salary);
        vacancy.setWorkShedule(workShedule);
        vacancy.setDistantWork(distantWork);
        vacancy.setAddress(address);
        vacancy.setDate_Time(date_Time);
        return vacancyRepository.save(vacancy);
    }
}