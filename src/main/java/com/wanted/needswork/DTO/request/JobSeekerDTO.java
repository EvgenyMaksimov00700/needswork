package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class JobSeekerDTO {
    @Getter
    private BigInteger user_id;
    @Getter
    private Double latitude;
    @Getter
    private Double longitude;




}
