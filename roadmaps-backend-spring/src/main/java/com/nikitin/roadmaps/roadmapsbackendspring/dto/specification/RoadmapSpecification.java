package com.nikitin.roadmaps.roadmapsbackendspring.dto.specification;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile_;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoadmapSpecification {

    public static Specification<Roadmap> filterBy(RoadmapFilter filter) {
        Specification<Roadmap> specification = (root, query, criteriaBuilder) -> null;

        if (!CollectionUtils.isEmpty(filter.getIds())) {
            specification = Optional.of(specification.and(inIds(filter.getIds())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getStartWithName())) {
            specification = Optional.of(specification.and(likeName(filter.getStartWithName())))
                    .orElse(specification);
        }

        if (!CollectionUtils.isEmpty(filter.getProfileIds())) {
            specification = Optional.of(specification.and(inProfileIds(filter.getProfileIds())))
                    .orElse(specification);
        }

        if (Objects.nonNull(filter.getCustom())) {
            specification = Optional.of(specification.and(equalsCustom(filter.getCustom())))
                    .orElse(specification);
        }

        return specification;
    }

    private static Specification<Roadmap> inIds(List<Long> ids) {
        return (root, query, criteriaBuilder) ->
                root.get(Roadmap_.id).in(ids.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    private static Specification<Roadmap> likeName(String startWithName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get(Roadmap_.name)), "%" + startWithName.toUpperCase() + "%");
    }

    private static Specification<Roadmap> inProfileIds(List<Long> profileIds) {
        return (root, query, criteriaBuilder) ->
                root.get(Roadmap_.profile).get(Profile_.id).in(profileIds.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    private static Specification<Roadmap> equalsCustom(Boolean custom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Roadmap_.custom), custom);
    }
}
