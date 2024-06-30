package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.ResponseResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vacancy vacancy;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private JobSeeker job_seeker;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private Integer date_time;


    public Response (Vacancy vacancy, JobSeeker job_seeker, String comment, Integer date_time) {
        this.vacancy = vacancy;
        this.job_seeker = job_seeker;
        this.comment = comment;
        this.date_time = date_time;
    }
    public ResponseResponseDTO toResponseDTO() {
      return new ResponseResponseDTO(this.id, this.vacancy.toResponseDTO(), this.job_seeker.toResponseDTO(),
                this.comment, this.date_time);
    }
}
