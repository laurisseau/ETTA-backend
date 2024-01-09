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
public class LessonPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private int pageNum;

    @Column
    private int lessonId;
    private String header;
    private String lessonInfo;
    private String task;
    private String editorLanguage;
    private String editorValue;
    public LessonPage(){

    }
}
