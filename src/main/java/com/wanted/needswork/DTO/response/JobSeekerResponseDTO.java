package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
@AllArgsConstructor
public class JobSeekerResponseDTO {
    @Getter
    private Integer id;
    @Getter
    private UserResponseDTO user;
    @Getter
    private Double latitude;
    @Getter
    private Double longitude;
    @Getter
    private String textResume;

}
