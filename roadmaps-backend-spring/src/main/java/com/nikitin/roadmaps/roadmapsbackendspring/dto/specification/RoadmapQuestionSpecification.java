package com.nikitin.roadmaps.roadmapsbackendspring.dto.specification;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion_;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic_;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.ExploredStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoadmapQuestionSpecification {

    public static Specification<RoadmapQuestion> filterBy(RoadmapQuestionFilter filter) {
        Specification<RoadmapQuestion> specification = (root, query, criteriaBuilder) -> null;

        if (!CollectionUtils.isEmpty(filter.getIds())) {
            specification = Optional.of(specification.and(inIds(filter.getIds())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getRoadmapTopicIds())) {
            specification = Optional.of(specification.and(inRoadmapTopicIds(filter.getRoadmapTopicIds())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getStartWithQuestion())) {
            specification = Optional.of(specification.and(likeQuestion(filter.getStartWithQuestion())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getStartWithAnswer())) {
            specification = Optional.of(specification.and(likeAnswer(filter.getStartWithAnswer())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getExploredStatuses())) {
            specification = Optional.of(specification.and(inExploredStatus(filter.getExploredStatuses())))
                    .orElse(specification);
        }

        return specification;
    }

    private static Specification<RoadmapQuestion> inIds(List<Long> ids) {
        return (root, query, criteriaBuilder) ->
                root.get(RoadmapQuestion_.id).in(ids.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    private static Specification<RoadmapQuestion> inRoadmapTopicIds(List<Long> roadmapTopicIds) {
        return (root, query, criteriaBuilder) ->
                root.get(RoadmapQuestion_.roadmapTopic).get(RoadmapTopic_.id).in(roadmapTopicIds.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    private static Specification<RoadmapQuestion> inExploredStatus(List<ExploredStatus> exploredStatuses) {
        return (root, query, criteriaBuilder) ->
                root.get(RoadmapQuestion_.EXPLORED_STATUS).in(exploredStatuses.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    private static Specification<RoadmapQuestion> likeQuestion(String startWithQuestion) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get(RoadmapQuestion_.question)),
                        "%" + startWithQuestion.toUpperCase() + "%"
                );
    }

    private static Specification<RoadmapQuestion> likeAnswer(String startWithAnswer) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(
                        root.get(RoadmapQuestion_.answer)),
                        "%" + startWithAnswer.toUpperCase() + "%"
                );
    }
}
