package com.wanted.needswork.models;

import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.IndustryResponseDTO;
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


    public Industry (String username, String category) {
        this.username = username;
        this.category = category;
    }
    public IndustryResponseDTO toResponseDTO() {
        return new IndustryResponseDTO(id, username, category);}
}
