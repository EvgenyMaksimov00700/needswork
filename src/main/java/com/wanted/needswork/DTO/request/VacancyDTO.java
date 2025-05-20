package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class VacancyDTO {

    @Getter
    private BigInteger employer_id;
    @Getter
    private String industry_id;
    @Getter
    private String position;
    @Getter
    private String city;
    @Getter
    private Integer fromSalary;
    @Getter
    private Integer toSalary;
    @Getter
    private String workSchedule;
    @Getter
    private Boolean distantWork;
    @Getter
    private String address;
    @Getter
    private String exp;
    @Getter
    private String responsibility;

}