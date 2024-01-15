package com.nikitin.roadmaps.roadmapsbackendspring.dto.specification;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter_;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoadmapChapterSpecification {

	public static Specification<RoadmapChapter> filterBy(RoadmapChapterFilter filter) {
		Specification<RoadmapChapter> specification = (root, query, criteriaBuilder) -> null;

		if (!CollectionUtils.isEmpty(filter.getIds())) {
			specification = Optional.of(specification.and(inIds(filter.getIds())))
					.orElse(specification);
		}

		if (!CollectionUtils.isEmpty(filter.getRoadmapIds())) {
			specification = Optional.of(specification.and(inRoadmapIds(filter.getRoadmapIds())))
					.orElse(specification);
		}

		if (Objects.nonNull(filter.getStartWithName())) {
			specification = Optional.of(specification.and(likeName(filter.getStartWithName())))
					.orElse(specification);
		}


		return specification;
	}

	private static Specification<RoadmapChapter> inIds(List<Long> ids) {
		return (root, query, criteriaBuilder) ->
				root.get(RoadmapChapter_.ID).in(ids.stream()
						.filter(Objects::nonNull)
						.collect(Collectors.toList()));
	}

	private static Specification<RoadmapChapter> inRoadmapIds(List<Long> roadmapIds) {
		return (root, query, criteriaBuilder) ->
				root.get(RoadmapChapter_.ROADMAP).get(Roadmap_.ID).in(roadmapIds.stream()
						.filter(Objects::nonNull)
						.collect(Collectors.toList()));
	}

	private static Specification<RoadmapChapter> likeName(String startWithName) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.like(criteriaBuilder.upper(root.get(RoadmapChapter_.name)), "%" + startWithName.toUpperCase() + "%");
	}
}
