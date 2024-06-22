package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class JobSeekerDTO {
    @Getter
    private Integer user_id;
    @Getter
    private String video_cv;
    @Getter
    private Double latitude;
    @Getter
    private Double longitude;




}
