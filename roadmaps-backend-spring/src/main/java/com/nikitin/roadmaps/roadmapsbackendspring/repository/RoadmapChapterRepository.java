package com.nikitin.roadmaps.roadmapsbackendspring.repository;

import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapChapterRepository extends JpaRepository<RoadmapChapter, Long>, JpaSpecificationExecutor<RoadmapChapter> {

}
