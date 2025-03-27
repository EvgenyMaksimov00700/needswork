package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

public class ResponseVacancyUserDTO {
    @Getter
    VacancyResponseDTO vacancy;
    @Getter
    UserResponseDTO user;
    @Getter
    String comment;
}
