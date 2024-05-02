package com.nikitin.roadmaps.roadmapsbackendspring.dto.specification;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter_;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoadmapTopicSpecification {

    public static Specification<RoadmapTopic> filterBy(RoadmapTopicFilter filter) {
        Specification<RoadmapTopic> specification = (root, query, criteriaBuilder) -> null;

        if (Objects.nonNull(filter.getRoadmapChapterIds())) {
            specification = Optional.of(specification.and(inRoadmapChapterIds(filter.getRoadmapChapterIds())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getStartWithName())) {
            specification = Optional.of(specification.and(likeName(filter.getStartWithName())))
                    .orElse(specification);
        }

        return specification;
    }

    private static Specification<RoadmapTopic> inRoadmapChapterIds(List<Long> roadmapChapterIds) {
        return (root, query, criteriaBuilder) ->
                root.get(RoadmapTopic_.roadmapChapter).get(RoadmapChapter_.id).in(roadmapChapterIds.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    private static Specification<RoadmapTopic> likeName(String startWithName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get(RoadmapTopic_.name)),
                        "%" + startWithName.toUpperCase() + "%"
                );
    }
}
