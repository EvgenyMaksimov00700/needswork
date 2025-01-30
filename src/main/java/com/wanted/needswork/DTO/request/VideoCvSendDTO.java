package com.wanted.needswork.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@NoArgsConstructor
@Getter
public class VideoCvSendDTO {
    private BigInteger userId;
    private String videoCvMessage;
    private Integer vacancyId;
    private Boolean textResume=false;
    public VideoCvSendDTO(BigInteger userId, String videoCvMessage, Integer vacancyId, Boolean textResume) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
        this.vacancyId = vacancyId;
        this.textResume=textResume;
    }



    public VideoCvSendDTO(BigInteger userId, String videoCvMessage, Integer vacancyId) {
        this.userId = userId;
        this.videoCvMessage = videoCvMessage;
        this.vacancyId = vacancyId;
    }

}
