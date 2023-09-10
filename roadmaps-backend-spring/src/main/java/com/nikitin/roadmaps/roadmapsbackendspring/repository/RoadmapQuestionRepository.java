package com.nikitin.roadmaps.roadmapsbackendspring.repository;

import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapQuestionRepository extends JpaRepository<RoadmapQuestion, Long>, JpaSpecificationExecutor<RoadmapQuestion> {

    Page<RoadmapQuestion> findAllByRoadmapTopicId(Long topicId, Pageable pageable);
}
