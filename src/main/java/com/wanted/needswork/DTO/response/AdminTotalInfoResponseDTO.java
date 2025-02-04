package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AdminTotalInfoResponseDTO {
    @Getter
    private Integer vacancies;
    @Getter
    private Integer responses;
    @Getter
    private Integer videoResponses;
    @Getter
    private Integer textResponses;
    @Getter
    private Integer withoutResponses;
    @Getter
    private Integer payments;
    @Getter
    private Integer jobSeekers;
    @Getter
    private Integer employers;
    @Getter
    private Integer averageVisitTimeSeconds;

}
