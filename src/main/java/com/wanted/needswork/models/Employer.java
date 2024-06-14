package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Integer user_id;

    @Getter
    @Setter
    private Integer inn;

    @Getter
    @Setter
    private Integer ogrn;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String logo;

    @Getter
    @Setter
    private String description;

    public Employer (Integer id, Integer user_id, Integer inn, Integer ogrn, String name, String logo, String description) {
        this.id = id;
        this.user_id = user_id;
        this.inn = inn;
        this.ogrn = ogrn;
        this.logo = logo;
        this.description = description;


    }
}
