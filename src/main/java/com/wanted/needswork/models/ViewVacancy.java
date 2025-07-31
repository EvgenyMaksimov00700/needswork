package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
import com.wanted.needswork.DTO.response.ViewVacancyResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor

public class ViewVacancy  {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Getter
    @Setter
    private BigInteger vacancyId;

    @CreationTimestamp
    @Getter
    private LocalDateTime viewDateTime;



    public ViewVacancy (User user, BigInteger vacancyId){
        this.user = user;
        this.vacancyId = vacancyId;
    }

    public ViewVacancyResponseDTO toResponseDTO(Vacancy vacancy) {
        return new ViewVacancyResponseDTO(id, user.toResponseDTO(), vacancy.toResponseDTO(), viewDateTime);
    }


}
