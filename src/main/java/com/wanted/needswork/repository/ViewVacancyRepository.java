package com.wanted.needswork.repository;

import com.wanted.needswork.models.ViewVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;


public interface ViewVacancyRepository extends JpaRepository <ViewVacancy, Integer> {
    List<ViewVacancy> findByUserId (BigInteger userId);
    List<ViewVacancy> findByUserIdAndVacancyId (BigInteger userId, BigInteger vacancyId);

    Integer countByVacancyId(BigInteger vacancyId);
}



