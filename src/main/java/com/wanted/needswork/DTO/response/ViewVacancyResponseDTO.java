package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class ViewVacancyResponseDTO {
    @Getter
    private Integer id;
    @Getter
    private UserResponseDTO user;
    @Getter
    private VacancyResponseDTO vacancy;
    @Getter
    private LocalDateTime viewDateTime;
}
