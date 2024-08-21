package com.wanted.needswork.services;


import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public Vacancy addVacancy(Employer employer_id, Industry industry_id, String position, String city, Integer fromSalary, Integer toSalary,
                              String workShedule, Boolean distantWork, String address, String exp, String responsibility) {
        Vacancy vacancy = new Vacancy(employer_id, industry_id, position, city, toSalary, fromSalary, exp, responsibility, workShedule, distantWork, address);
        return vacancyRepository.save(vacancy);
    }

    public Vacancy updateVacancy(Vacancy vacancy, Employer employer_id, Industry industry_id, String position, String city,
                                 Integer fromSalary, Integer toSalary, String workSchedule, Boolean distantWork, String address, String exp, String responsibility) {
        if (employer_id != null) {
            vacancy.setEmployer(employer_id);
        }
        if (industry_id != null) {
            vacancy.setIndustry(industry_id);
        }
        if (position != null) {
            vacancy.setPosition(position);
        }
        if (city != null) {
            vacancy.setCity(city);
        }
        if (toSalary != null) {
            vacancy.setToSalary(toSalary);
        }
        if (fromSalary != null) {
            vacancy.setFromSalary(fromSalary);
        }
        if (exp != null) {
            vacancy.setExp(exp);
        }
        if (responsibility != null) {
            vacancy.setResponsibility(responsibility);
        }
        if (workSchedule != null) {
            vacancy.setWorkSchedule(workSchedule);
        }
        if (distantWork != null) {
            vacancy.setDistantWork(distantWork);
        }
        if (address != null) {
            vacancy.setAddress(address);
        }


        return vacancyRepository.save(vacancy);
    }

    public List<String> getCities() {
        String filePath = "src/main/resources/static/text/cities.txt";

        // Список для хранения городов
        List<String> cityList = new ArrayList<>();

        // Чтение файла


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                cityList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityList;
    }
}

