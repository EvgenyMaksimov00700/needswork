package com.wanted.needswork.DTO.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.models.JobSeeker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor

public class VideoCvResponseDTO {

    @Getter
    private Integer id;

    @Getter
    private JobSeekerResponseDTO jobSeeker;

    @Getter
    private String video_message;

    @Getter
    private String name;


}


