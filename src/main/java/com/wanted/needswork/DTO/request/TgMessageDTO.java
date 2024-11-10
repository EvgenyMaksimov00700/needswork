package com.wanted.needswork.DTO.request;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class TgMessageDTO {
    private BigInteger userId;
    private String message;

    public TgMessageDTO(BigInteger userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}