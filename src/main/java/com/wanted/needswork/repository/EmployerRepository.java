package com.wanted.needswork.repository;

import com.wanted.needswork.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.math.BigInteger;

public interface EmployerRepository extends JpaRepository <Employer,BigInteger> {
    Employer findByUserId(BigInteger userId);

    Employer findByInn(BigInteger inn);

    Employer findByEmail(String email);
}


