package com.nikitin.roadmaps.roadmapsbackendspring.repository;

import com.nikitin.roadmaps.roadmapsbackendspring.entity.NewsCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCardRepository extends JpaRepository<NewsCard, Long>, JpaSpecificationExecutor<NewsCard> {
}
