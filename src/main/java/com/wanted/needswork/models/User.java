package com.wanted.needswork.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
@Table(name="\"user\"")
public class User {
    @Id
    @Getter
    @Setter
    private BigInteger id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER)
    Employer employer;

    @Getter
    @Setter
    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    JobSeeker jobSeeker;

    public User (BigInteger ID, String fullName, String username) {
        this.id = ID;
        this.fullName = fullName;
        this.username = username;
        this.phone = null;
    }

    public UserResponseDTO toResponseDTO() {
        return new UserResponseDTO(id, username, fullName, phone);
    }
}

