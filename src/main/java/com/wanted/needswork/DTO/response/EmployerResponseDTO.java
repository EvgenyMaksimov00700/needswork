package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
public class EmployerResponseDTO {
    @Getter
    private BigInteger employer_id;
    @Getter
    private UserResponseDTO user_id;
    @Getter
    private BigInteger inn;
    @Getter
    private BigInteger ogrn;
    @Getter
    private String name;
    @Getter
    private String logo;
    @Getter
    private String description;
    @Getter
    private String email;
    @Getter
    private String phone;


    public EmployerResponseDTO(String name, String logo, String email) {
        this.name = name;
        this.logo = logo;
        this.employer_id = null;
        this.user_id = null;
        this.inn = null;
        this.ogrn = null;
        this.email = email;
    }

}


