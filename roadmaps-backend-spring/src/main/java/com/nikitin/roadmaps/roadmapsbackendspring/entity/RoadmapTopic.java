package com.nikitin.roadmaps.roadmapsbackendspring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roadmap_topic")
public class RoadmapTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "number_of_question", columnDefinition = "int8")
    private Integer numberOfQuestion = 0;

    @Column(name = "number_explored_question", columnDefinition = "int8")
    private Integer numberExploredQuestion = 0;

    @Column(name = "position", nullable = false, columnDefinition = "int8")
    private Long position;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "roadmap_chapter_id", nullable = false, columnDefinition = "int8")
    private RoadmapChapter roadmapChapter;
}
