package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Vacancy {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employer employer;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Industry industry;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private Integer salary;

    @Getter
    @Setter
    private String workSchedule;

    @Getter
    @Setter
    private Boolean distantWork;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private Integer date_Time;


    public Vacancy(Employer employer, Industry industry, String position, String city, Integer salary, String workSchedule, Boolean distantWork, String address, Integer date_Time) {
        this.employer = employer;
        this.industry = industry;
        this.position = position;
        this.city = city;
        this.salary = salary;
        this.workSchedule = workSchedule;
        this.distantWork = distantWork;
        this.address = address;
        this.date_Time = date_Time;


    }

    public VacancyResponseDTO toResponseDTO() {
        return new VacancyResponseDTO(id, employer.toResponseDTO(), industry.toResponseDTO(), position, city, salary, workSchedule, distantWork, address, date_Time);
    }
}