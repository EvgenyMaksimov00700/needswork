package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanted.needswork.DTO.response.JobSeekerResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Entity
@NoArgsConstructor
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    @Getter
    @Setter
    private Double latitude;

    @Getter
    @Setter
    private Double longitude;

    @Getter
    @Setter
    private String textResume;

    public JobSeeker(User user, Double latitude, Double longitude) {

        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public JobSeekerResponseDTO toResponseDTO() {
        return new JobSeekerResponseDTO(id, user.toResponseDTO(), latitude, longitude, textResume);
    }
}
