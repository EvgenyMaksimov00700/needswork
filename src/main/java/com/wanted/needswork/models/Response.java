package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Integer vacancy_id;

    @Getter
    @Setter
    private Integer job_seeker_id;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private Integer date_time;


    public Response (Integer id, Integer vacancy_id, Integer job_seeker_id, String comment, Integer date_time) {
        this.id = id;
        this.vacancy_id = vacancy_id;
        this.job_seeker_id = job_seeker_id;
        this.comment = comment;
        this.date_time = date_time;
    }
}
