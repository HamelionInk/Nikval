package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.NewsCardRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.NewsCardResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.NewsCard;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface NewsCardService {

    NewsCardResponseDto create(@NonNull NewsCardRequestDto newsCardRequestDto);

    NewsCardResponseDto patch(@NonNull Long id, @NonNull NewsCardRequestDto newsCardRequestDto);

    NewsCard getEntityById(@NonNull Long id);

    NewsCardResponseDto getResponseById(@NonNull Long id);

    Page<NewsCardResponseDto> getAll(@NonNull Pageable pageable);

    void deleteById(@NonNull Long id);
}
