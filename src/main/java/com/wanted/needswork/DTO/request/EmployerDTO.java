package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class EmployerDTO {
    @Getter
    private BigInteger user_id;
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

    public EmployerDTO(BigInteger user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }
}


