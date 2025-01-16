package com.wanted.needswork.DTO.request;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class VideoCvSendDTO {
    private BigInteger userId;
    private String videoCvMessage;
    private Integer vacancyId;

    public VideoCvSendDTO(BigInteger userId, String videoCvMessage, Integer vacancyId) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
        this.vacancyId = vacancyId;
    }

}
