package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Getter
    @Setter
    private BigInteger inn;

    @Getter
    @Setter
    private BigInteger ogrn;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String logo;

    @Getter
    @Setter
    private String description;

    public Employer (User user, BigInteger inn, BigInteger ogrn, String name, String logo, String description) {
        this.user = user;
        this.inn = inn;
        this.ogrn = ogrn;
        this.logo = logo;
        this.description = description;
        this.name = name;

    }

   public EmployerResponseDTO toResponseDTO() {
       return new EmployerResponseDTO(id, user.toResponseDTO(), inn, ogrn, name, logo, description);
  }
}
