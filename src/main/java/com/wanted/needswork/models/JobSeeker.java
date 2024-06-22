package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class JobSeeker {



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
    private String video_cv;

    @Getter
    @Setter
    private Double location;


    public JobSeeker (Integer user_id, String video_cv, Double location) {

        this.user_id = user_id;
        this.video_cv = video_cv;
        this.location = location;
    }
}
