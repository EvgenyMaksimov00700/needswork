package com.wanted.needswork.repository;

import com.wanted.needswork.models.City;
import com.wanted.needswork.models.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository <City, String> {

    City findByName(String name);
}