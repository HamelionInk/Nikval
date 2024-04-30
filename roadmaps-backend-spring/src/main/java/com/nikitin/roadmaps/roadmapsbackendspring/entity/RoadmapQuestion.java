package com.nikitin.roadmaps.roadmapsbackendspring.entity;

import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.ExploredStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity(name = "roadmap_question")
public class RoadmapQuestion extends BaseEntity {

    @Column(name = "question", nullable = false, columnDefinition = "text")
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "text")
    private String answer;

    @Column(name = "explored_status", nullable = false, columnDefinition = "varchar(25)")
    @Enumerated(value = EnumType.STRING)
    private ExploredStatus exploredStatus;

    @Column(name = "position", nullable = false, columnDefinition = "int8")
    private Long position;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "roadmap_topic_id", nullable = false, columnDefinition = "int8")
    private RoadmapTopic roadmapTopic;
}
