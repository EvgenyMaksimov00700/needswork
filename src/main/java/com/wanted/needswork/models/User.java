package com.wanted.needswork.models;

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

    public User (BigInteger ID, String fullName, String phone, String username) {
        this.id = ID;
        this.fullName = fullName;
        this.username = username;
        this.phone = phone;
    }
}
