package com.nikitin.roadmaps.roadmapsbackendspring.repository;

import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapTopicRepository extends JpaRepository<RoadmapTopic, Long>, JpaSpecificationExecutor<RoadmapTopic> {

    Page<RoadmapTopic> findAllByRoadmapChapterId(Long chapterId, Pageable pageable);
}
