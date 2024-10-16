package com.wanted.needswork.DTO.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.models.JobSeeker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor

public class VideoCvDTO {

    @Getter
    private Integer job_seeker_id;

    @Getter
    private String video_message;

    @Getter
    private String name;


}
