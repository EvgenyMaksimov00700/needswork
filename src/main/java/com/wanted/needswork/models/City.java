package com.wanted.needswork.models;

import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor

public class City {
    @Id
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String name;



    public City (Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
