package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
@AllArgsConstructor
public class EmployerResponseDTO {
    @Getter
    private Integer employer_id;
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
}
