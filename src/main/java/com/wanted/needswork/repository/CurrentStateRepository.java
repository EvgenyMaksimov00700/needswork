package com.wanted.needswork.repository;

import com.wanted.needswork.models.CurrentState;
import com.wanted.needswork.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.math.BigInteger;

public interface CurrentStateRepository extends JpaRepository <CurrentState, Integer> {
    CurrentState findByUserId (BigInteger userId);

}


