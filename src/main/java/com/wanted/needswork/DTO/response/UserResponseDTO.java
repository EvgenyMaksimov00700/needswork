package com.wanted.needswork.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
@AllArgsConstructor
public class UserResponseDTO {
    @Getter
    private BigInteger id;
    @Getter
    private String username;
    @Getter
    private String fullName;
    @Getter
    private String phone;
}
