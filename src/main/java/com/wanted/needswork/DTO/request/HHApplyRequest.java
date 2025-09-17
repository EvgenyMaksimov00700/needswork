package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
public class HHApplyRequest {
    @Getter
    BigInteger vacancyId;
    @Getter
    String responseId;
}
