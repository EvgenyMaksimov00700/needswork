package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String category;


    public Industry (Integer id, String username, String category) {
        this.id = id;
        this.username = username;
        this.category = category;
    }
}
