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

public class Industry {
    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String category;



    public Industry (String username, String category) {
        this.username = username;
        this.category = category;
    }

    public Industry(String id, String username, String category) {
        this.id = id;
        this.username = username;
        this.category = category;
    }


    public IndustryResponseDTO toResponseDTO() {
        return new IndustryResponseDTO(id, username, category);}
}
