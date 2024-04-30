package com.nikitin.roadmaps.roadmapsbackendspring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "roadmap_chapter")
public class RoadmapChapter extends BaseEntity {

    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "position", nullable = false, columnDefinition = "int8")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long position;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "roadmap_id", nullable = false, columnDefinition = "int8")
    private Roadmap roadmap;
}
