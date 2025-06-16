package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
public class StateSendRequestDTO {
    @Getter
    Integer stateId;
    @Getter
    BigInteger vacancyId;
    @Getter
    BigInteger userId;
}


