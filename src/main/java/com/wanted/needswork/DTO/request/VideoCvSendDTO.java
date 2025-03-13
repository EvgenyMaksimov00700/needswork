package com.wanted.needswork.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@NoArgsConstructor
@Getter
public class VideoCvSendDTO {
    private BigInteger userId;
    private String videoCvMessage;
    private BigInteger vacancyId;
    private Boolean textResume=false;
    public VideoCvSendDTO(BigInteger userId, String videoCvMessage, BigInteger vacancyId, Boolean textResume) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
        this.vacancyId = vacancyId;
        this.textResume=textResume;
    }



    public VideoCvSendDTO(BigInteger userId, String videoCvMessage, BigInteger vacancyId) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
        this.vacancyId = vacancyId;
    }

}
