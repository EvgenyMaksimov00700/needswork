package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class IndustryResponseDTO {
    @Getter
    private String id;

    @Getter
    private String name;
    @Getter
    private String category;

}
