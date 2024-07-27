package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

public class UserWithoutPhoneDTO {

        @Getter
        private BigInteger id;
        @Getter
        private String username;
        @Getter
        private String fullName;



}
