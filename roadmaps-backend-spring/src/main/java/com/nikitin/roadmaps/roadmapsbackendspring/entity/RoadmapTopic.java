package com.nikitin.roadmaps.roadmapsbackendspring.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RoadmapTopic")
@Table(name = "roadmap_topic")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_chapter_id", nullable = false, columnDefinition = "int8")
    private RoadmapChapter roadmapChapter;

    @OneToMany(mappedBy = "roadmapTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapQuestion> roadmapQuestions = new ArrayList<>();

    public void incrementExploredQuestion() {
        numberExploredQuestion++;
    }

    public void incrementNumberOfQuestion() {
        numberOfQuestion++;
    }

    public void decrementNumberOfQuestion() {
        numberOfQuestion--;
    }

    public void addRoadmapQuestion(RoadmapQuestion roadmapQuestion) {
        roadmapQuestions.add(roadmapQuestion);
        roadmapQuestion.setRoadmapTopic(this);
    }

    public void removeRoadmapQuestion(RoadmapQuestion roadmapQuestion) {
        roadmapQuestions.remove(roadmapQuestion);
        roadmapQuestion.setRoadmapTopic(null);
    }
}
