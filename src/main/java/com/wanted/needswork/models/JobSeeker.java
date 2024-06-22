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
    private Double latitude;

    @Getter
    @Setter
    private Double longitude;

    public JobSeeker (Integer user_id, String video_cv, Double latitude, Double longitude) {

        this.user_id = user_id;
        this.video_cv = video_cv;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
