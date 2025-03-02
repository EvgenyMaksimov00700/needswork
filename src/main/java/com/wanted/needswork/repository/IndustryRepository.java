package com.wanted.needswork.repository;

import com.wanted.needswork.models.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository <Industry,String> {

}