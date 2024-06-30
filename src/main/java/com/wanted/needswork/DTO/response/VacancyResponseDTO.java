package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class VacancyResponseDTO {
    @Getter
    private Integer id;
    @Getter
    private EmployerResponseDTO employer;
    @Getter
    private IndustryResponseDTO industry;
    @Getter
    private String position;
    @Getter
    private String city;
    @Getter
    private Integer salary;
    @Getter
    private String workSchedule;
    @Getter
    private Boolean distantWork;
    @Getter
    private String address;
    @Getter
    private Integer date_Time;


}
