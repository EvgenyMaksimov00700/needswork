package com.wanted.needswork.repository;

import com.wanted.needswork.models.Response;
import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository <Response, Integer> {

}
