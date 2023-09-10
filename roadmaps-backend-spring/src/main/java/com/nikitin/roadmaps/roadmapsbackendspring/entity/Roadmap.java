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
@Entity(name = "Roadmap")
@Table(name = "roadmap")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, columnDefinition = "int8")
    private Profile profile;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapChapter> roadmapChapters = new ArrayList<>();

    public void addRoadmapChapter(RoadmapChapter roadmapChapter) {
        roadmapChapters.add(roadmapChapter);
        roadmapChapter.setRoadmap(this);
    }

    public void removeRoadmapChapter(RoadmapChapter roadmapChapter) {
        roadmapChapters.remove(roadmapChapter);
        roadmapChapter.setRoadmap(null);
    }
}