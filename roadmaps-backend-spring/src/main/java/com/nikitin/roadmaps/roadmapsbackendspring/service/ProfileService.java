package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.ProfileRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    ProfileResponseDto create(@NonNull ProfileRequestDto profileRequestDto);
    ProfileResponseDto update(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto);
    ProfileResponseDto patch(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto);
    ProfileResponseDto getById(@NonNull Long id);
    ProfileResponseDto getByEmail(@NonNull String email);
    Page<ProfileResponseDto> getAll(@NonNull Pageable pageable);
}
