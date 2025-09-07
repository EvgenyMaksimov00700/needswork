package com.wanted.needswork.repository;

import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ResponseRepository extends JpaRepository <Response, Integer> {

    List<Response> findAllResponsesByVacancyId(BigInteger vacancy_id);
}

