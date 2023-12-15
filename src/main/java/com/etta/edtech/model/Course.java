package com.etta.edtech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String courseId;
    private String educatorId;
    private String subscriptionId;
    private String subscription;
    private int studentAmount;


    public Course() {
        this.courseId = UUID.randomUUID().toString();
        this.studentAmount = 0;
    }

}
