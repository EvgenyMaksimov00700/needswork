package com.wanted.needswork.repository;

import com.wanted.needswork.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository <Employer,Integer> {

}
