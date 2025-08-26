package com.wanted.needswork.DTO.request;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class JobSeekerUserDTO {
    private BigInteger user_id;
    private String fullName;
    private String username;
    private Double latitude;
    private Double longitude;
}

