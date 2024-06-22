package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class EmployerDTO {
    @Getter
    private Integer user_id;
    @Getter
    private Integer inn;
    @Getter
    private Integer ogrn;
    @Getter
    private String name;
    @Getter
    private String logo;
    @Getter
    private String description;

}
