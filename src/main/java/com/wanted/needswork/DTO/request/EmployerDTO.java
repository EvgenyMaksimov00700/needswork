package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO {
    @Getter
    @Setter
    private BigInteger user_id;
    @Getter
    @Setter
    private BigInteger inn;
    @Getter
    @Setter
    private BigInteger ogrn;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private MultipartFile logo;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;

    public EmployerDTO(BigInteger user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }
}


