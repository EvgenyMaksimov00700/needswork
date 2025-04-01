package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
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

    @Getter
    @Setter
    private String email;

    public Employer (User user, BigInteger inn, BigInteger ogrn, String name, String logo, String description, String email) {
        this.user = user;
        this.inn = inn;
        this.ogrn = ogrn;
        this.logo = logo;
        this.description = description;
        this.name = name;
        this.email = email;

    }
    public Employer (String name, String logo, String email) {
        this.name = name;
        this.logo = logo;
        this.inn = null;
        this.ogrn = null;
        this.email = email;
    }
   public EmployerResponseDTO toResponseDTO() {
       UserResponseDTO userResponseDTO = null;
       if(user!=null) {
           userResponseDTO = user.toResponseDTO();
       }
       return new EmployerResponseDTO(id, userResponseDTO, inn, ogrn, name, logo, description, email);
  }
}
