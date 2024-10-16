package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.JobSeekerResponseDTO;
import com.wanted.needswork.DTO.response.VideoCvResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class VideoCv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private  JobSeeker jobSeeker;

    @Getter
    @Setter
    private String video_message;

    @Getter
    @Setter
    private String name;

    public VideoCv(JobSeeker jobSeeker, String video_message, String name) {
        this.jobSeeker = jobSeeker;
        this.video_message = video_message;
        this.name = name;
    }
    public VideoCvResponseDTO toResponseDTO() {
        return new VideoCvResponseDTO(id, jobSeeker.toResponseDTO(), video_message, name);
    }
}
