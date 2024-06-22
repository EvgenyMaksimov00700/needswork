package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
public class ResponseDTO {

    @Getter
    private Integer vacancy_id;
    @Getter
    private Integer job_seeker_id;
    
    @Getter
    private String comment;
    @Getter
    private Integer date_time;


}
