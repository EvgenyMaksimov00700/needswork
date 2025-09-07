package com.wanted.needswork.repository;

import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface VacancyRepository extends JpaRepository <Vacancy, BigInteger> {

    List<Vacancy> findAllByEmployer_UserId(BigInteger userId);

    List<Vacancy> findAllByEmployerId(BigInteger userId);
}
