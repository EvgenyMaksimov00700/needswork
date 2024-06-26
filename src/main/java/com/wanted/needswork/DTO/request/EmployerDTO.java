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

}
