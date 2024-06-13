package com.wanted.needswork.repository;

import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Integer> {

}
