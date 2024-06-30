package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class VacancyDTO {

    @Getter
    private Integer employer_id;
    @Getter
    private Integer industry_id;
    @Getter
    private String position;
    @Getter
    private String city;
    @Getter
    private Integer salary;
    @Getter
    private String workShedule;
    @Getter
    private Boolean distantWork;
    @Getter
    private String address;

}