package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
public class AdminEmployerInfoResponseDTO {
    @Getter
    private Integer amountVacancy;

    @Getter
    private Integer amountResponses;

    @Getter
    private Integer amountPayedVacancies;

    @Getter
    private List<VacancyResponseDTO> responses;






}
