package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.BadRequestException;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.ProfileMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.ProfileRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileServiceImplement implements ProfileService {

    private static final String PROFILE_ALREADY_EXIST = "Профиль с указанной почтой уже используется - %s";
    private static final String PROFILE_NOT_FOUND = "Профиль с указанной почтой не найден - %s";

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponseDto create(@NonNull ProfileRequestDto profileRequestDto) {
        Optional.ofNullable(profileRequestDto.getEmail())
                .flatMap(profileRepository::findByEmailIgnoreCase)
                .ifPresent(profile -> {
                    throw new BadRequestException(String.format(PROFILE_ALREADY_EXIST, profileRequestDto.getEmail()));
                });

        var profile = profileMapper.toEntity(profileRequestDto);

        return profileMapper.toResponseDto(profileRepository.save(profile));
    }

    @Override
    public ProfileResponseDto update(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto) {
        return null;
    }

    @Override
    public ProfileResponseDto patch(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto) {
        return null;
    }

    @Override
    public ProfileResponseDto getById(@NonNull Long id) {
        return null;
    }

    @Override
    public ProfileResponseDto getByEmail(@NonNull String email) {
        return profileRepository.findByEmailIgnoreCase(email)
                .map(profileMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_NOT_FOUND, email)));
    }

    @Override
    public Page<ProfileResponseDto> getAll(@NonNull Pageable pageable) {
        return null;
    }
}
