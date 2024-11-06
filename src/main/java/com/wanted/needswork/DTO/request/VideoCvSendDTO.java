package com.wanted.needswork.DTO.request;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class VideoCvSendDTO {
    private BigInteger userId;
    private Integer videoCvId;


    public VideoCvSendDTO(BigInteger userId, Integer videoCvId) {
        this.userId = userId;
        this.videoCvId = videoCvId;
    }

}
