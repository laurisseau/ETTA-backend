package com.etta.edtech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class Enrolled {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column
    private String cognitoUserId;
    private String cognitoName;
    private String cognitoEmail;
    public Enrolled(){

    }
}
