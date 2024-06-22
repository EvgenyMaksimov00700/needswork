package com.wanted.needswork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
public class Vacancy {
    @Id

    @Getter
    @Setter
    private BigInteger id;

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
    private Integer salary;

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


    public Vacancy(Integer employer_id, Integer employerId, String position, String city, Integer salary, String workShedule, String distantWork, String address, Integer date_Time) {
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
