package com.wanted.needswork.services;

import com.wanted.needswork.models.City;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.User;
import com.wanted.needswork.repository.CityRepository;
import com.wanted.needswork.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service

public class CityService {
    @Autowired
    CityRepository cityRepository;
    public List<City> getCities() {
        return cityRepository.findAll();
    }
    public City addCity(Integer id, String name) {
        City city = new City(id, name);
        return cityRepository.save(city);
    }
    public City getCityByName(String name){
        return cityRepository.findByName(name);
    }
}