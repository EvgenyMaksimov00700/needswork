package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Integer employer_id;

    @Getter
    @Setter
    private Integer industry_id;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private Integer salary ;

    @Getter
    @Setter
    private String workShedule;

    @Getter
    @Setter
    private String distantWork;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private Integer date_Time;


    public Vacancy (Integer id, Integer employer_id, Integer industry_id, String position, String city, Integer salary, String workShedule, String distantWork, String address, Integer date_Time) {
        this.id = id;
        this.employer_id = employer_id;
        this.industry_id = industry_id;
        this.position = position;
        this.city = city;
        this.salary = salary;
        this.workShedule = workShedule;
        this.distantWork = distantWork;
        this.address = address;
        this.date_Time = date_Time;


    }
}
