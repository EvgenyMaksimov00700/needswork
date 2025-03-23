package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.ResponseResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

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
    private BigInteger vacancyId;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private JobSeeker job_seeker;

    @Getter
    @Setter
    private String comment;

    @CreationTimestamp
    @Getter
    private LocalDateTime createdDateTime;


    public Response (BigInteger vacancy_id, JobSeeker job_seeker, String comment) {
        this.vacancyId = vacancy_id;
        this.job_seeker = job_seeker;
        this.comment = comment;

    }
    public ResponseResponseDTO toResponseDTO() {
      return new ResponseResponseDTO(this.id, this.vacancyId, this.job_seeker.toResponseDTO(),
                this.comment, this.createdDateTime);
    }
}
