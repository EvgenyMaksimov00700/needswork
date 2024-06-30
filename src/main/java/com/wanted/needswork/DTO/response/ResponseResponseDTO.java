package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@AllArgsConstructor
public class ResponseResponseDTO {
    @Getter
    private Integer id;
    @Getter
    private VacancyResponseDTO vacancy;
    @Getter
    private JobSeekerResponseDTO job_seeker;


    @Getter
    private String comment;
    @Getter
    private LocalDateTime created_at;


}
