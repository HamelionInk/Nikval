package com.nikitin.roadmaps.roadmapsbackendspring.dto.specification;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter_;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.Optional;

public class RoadmapTopicSpecification {

    public static Specification<RoadmapTopic> filterBy(RoadmapTopicFilter filter) {
        Specification<RoadmapTopic> specification = (root, query, criteriaBuilder) -> null;

        if (Objects.nonNull(filter.getRoadmapChapterId())) {
            specification = Optional.of(specification.and(equalsId(filter.getRoadmapChapterId())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getStartWithName())) {
            specification = Optional.of(specification.and(likeName(filter.getStartWithName())))
                    .orElse(specification);
        }

        return specification;
    }

    private static Specification<RoadmapTopic> equalsId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(RoadmapTopic_.roadmapChapter).get(RoadmapChapter_.id), id);
    }

    private static Specification<RoadmapTopic> likeName(String startWithName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(RoadmapTopic_.name), startWithName);
    }
}
