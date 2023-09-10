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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RoadmapChapter")
@Table(name = "roadmap_chapter")
public class RoadmapChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false, columnDefinition = "int8")
    private Roadmap roadmap;

    @OneToMany(mappedBy = "roadmapChapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapTopic> roadmapTopics = new ArrayList<>();

    public void addRoadmapTopic(RoadmapTopic roadmapTopic) {
        roadmapTopics.add(roadmapTopic);
        roadmapTopic.setRoadmapChapter(this);
    }

    public void removeRoadmapTopic(RoadmapTopic roadmapTopic) {
        roadmapTopics.remove(roadmapTopic);
        roadmapTopic.setRoadmapChapter(null);
    }
}
