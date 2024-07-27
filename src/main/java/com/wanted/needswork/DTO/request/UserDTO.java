package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class UserDTO {
    @Getter
    private BigInteger id;
    @Getter
    private String username;
    @Getter
    private String fullName;
    @Getter
    private String phone;

    UserDTO(BigInteger id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }

}
