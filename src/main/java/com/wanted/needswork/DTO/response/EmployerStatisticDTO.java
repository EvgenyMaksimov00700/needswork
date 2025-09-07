package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

public class EmployerStatisticDTO {
    @Getter
    Integer totalViews;
    @Getter
    Integer totalResponses;
    @Getter
    Integer activeVacancies;
}
