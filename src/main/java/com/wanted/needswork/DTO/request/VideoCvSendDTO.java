package com.wanted.needswork.DTO.request;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class VideoCvSendDTO {
    private BigInteger userId;
    private String videoCvMessage;


    public VideoCvSendDTO(BigInteger userId, String videoCvMessage) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
    }

}
