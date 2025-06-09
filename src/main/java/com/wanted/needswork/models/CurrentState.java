package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.CurrentStateResponseDTO;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@NoArgsConstructor

public class CurrentState  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @Getter
    @Setter
    private BigInteger vacancyId;

    @Getter
    @Setter
    private String urlParams;




    public CurrentState (User user, BigInteger vacancyId, String urlParams) {
        this.user = user;
        this.vacancyId = vacancyId;
        this.urlParams = urlParams;
    }

    public CurrentStateResponseDTO toResponseDTO(Vacancy vacancy) {
        return new CurrentStateResponseDTO(id, user.toResponseDTO(), vacancy.toResponseDTO(), urlParams);
    }

}
