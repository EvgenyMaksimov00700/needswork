package com.wanted.needswork.repository;

import com.wanted.needswork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository <User, BigInteger> {

}
