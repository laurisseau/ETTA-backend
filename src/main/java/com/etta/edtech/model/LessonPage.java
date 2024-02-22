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




    @Column(columnDefinition = "TEXT")
    private String lessonInfo;

    @Column(columnDefinition = "TEXT")
    private String editorValue;

    @Column(columnDefinition = "TEXT")
    private String task;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lessonId;

    @Column
    private String header;

    @Column
    private String subHeader;

    private int pageNum;
    public LessonPage(){

    }
}
