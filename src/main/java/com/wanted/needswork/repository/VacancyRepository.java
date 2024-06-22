package com.wanted.needswork.repository;

import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface VacancyRepository extends JpaRepository <Vacancy, Integer> {

}
