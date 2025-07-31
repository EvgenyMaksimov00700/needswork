package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@AllArgsConstructor
@NoArgsConstructor
public class ViewVacancyDTO {
    @Getter
    private BigInteger userId;
    @Getter
    private BigInteger vacancyId;
}
