package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;


@AllArgsConstructor
public class ResponseDTO {

    @Getter
    private BigInteger vacancy_id;
    @Getter
    private Integer job_seeker_id;
    
    @Getter
    private String comment;



}
