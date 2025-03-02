package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Vacancy {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigInteger id;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Employer employer;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Industry industry;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private Integer fromSalary;

    @Getter
    @Setter
    private Integer toSalary;

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
    private String exp;

    @Getter
    @Setter
    @Column (length =10000)
    private String responsibility;

    @CreationTimestamp
    @Getter
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Getter
    private LocalDateTime lastModifiedDateTime;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Response> responses = new ArrayList<>();

    public Vacancy(Employer employer, Industry industry, String position, String city, Integer toSalary, Integer fromSalary, String exp, String responsibility, String workSchedule, Boolean distantWork, String address) {
        this.employer = employer;
        this.industry = industry;
        this.position = position;
        this.city = city;
        this.toSalary = toSalary;
        this.fromSalary = fromSalary;
        this.exp = exp;
        this.responsibility = responsibility;
        this.workSchedule = workSchedule;
        this.distantWork = distantWork;
        this.address = address;



    }

    public VacancyResponseDTO toResponseDTO() {
        return new VacancyResponseDTO(id, employer.toResponseDTO(), industry.toResponseDTO(), position, city, fromSalary, toSalary,  workSchedule, distantWork, address, exp, responsibility, createdDateTime, lastModifiedDateTime, false);
    }
}

