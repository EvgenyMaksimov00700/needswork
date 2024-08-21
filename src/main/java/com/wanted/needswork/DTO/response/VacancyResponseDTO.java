package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
    @Getter
    private LocalDateTime createdDateTime;
    @Getter
    private LocalDateTime lastModifiedDateTime;

}
