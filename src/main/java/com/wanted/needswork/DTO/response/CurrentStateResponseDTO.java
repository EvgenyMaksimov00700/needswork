package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
@AllArgsConstructor
public class CurrentStateResponseDTO {
    @Getter
    private Integer id;
    @Getter
    private UserResponseDTO user;
    @Getter
    private VacancyResponseDTO vacancy;
    @Getter
    private String URLParams;

}