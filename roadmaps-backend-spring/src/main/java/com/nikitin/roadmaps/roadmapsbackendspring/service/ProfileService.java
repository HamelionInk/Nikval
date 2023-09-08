package com.nikitin.roadmaps.roadmapsbackendspring.service;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    ProfileResponseDto create(@NonNull ProfileRequestDto profileRequestDto);

    String uploadAvatar(@NonNull Long id, @NonNull MultipartFile image);

    ProfileResponseDto patch(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto);

    Profile getEntityById(@NonNull Long id);

    ProfileResponseDto getResponseById(@NonNull Long id);

    ProfileResponseDto getByEmail(@NonNull String email);

    Page<ProfileResponseDto> getAll(@NonNull Pageable pageable);
}
