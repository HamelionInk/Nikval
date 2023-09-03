package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Profile;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.BadRequestException;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.NotFoundException;
import com.nikitin.roadmaps.roadmapsbackendspring.mapper.ProfileMapper;
import com.nikitin.roadmaps.roadmapsbackendspring.repository.ProfileRepository;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileServiceImplement implements ProfileService {

    private static final String PROFILE_ALREADY_EXIST = "Профиль с указанной почтой уже используется - %s";
    private static final String PROFILE_WITH_EMAIL_NOT_FOUND = "Профиль с указанной почтой не найден - %s";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Профиль с указанным идентификатором не найден - %s";
    private static final String UPLOAD_AVATAR_INVALID_FORMAT = "Неверный формат файла - %s";

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponseDto create(@NonNull ProfileRequestDto profileRequestDto) {
        emailAlreadyExist(profileRequestDto.getEmail(), null);

        var profile = profileMapper.toEntity(profileRequestDto);

        return profileMapper.toResponseDto(profileRepository.save(profile));
    }

    @Override
    public String uploadAvatar(@NonNull Long id, @NonNull MultipartFile image) {
        validateContentTypeUploadAvatar(image.getContentType());

        try {
            var imageData = image.getResource().getContentAsByteArray();
            var encodeImageData = Base64.encodeBase64String(imageData);

            patch(id, ProfileRequestDto.builder()
                    .picture("data:" + image.getContentType() + ";base64," + encodeImageData)
                    .build());

            return encodeImageData;
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @Override
    public ProfileResponseDto patch(@NonNull Long id, @NonNull ProfileRequestDto profileRequestDto) {
        return profileRepository.findById(id)
                .map(profileForUpdate -> {
                    emailAlreadyExist(profileRequestDto.getEmail(), profileForUpdate.getEmail());

                    return profileMapper.toResponseDto(profileRepository.save(profileMapper.toPatchEntity(profileRequestDto, profileForUpdate)));
                })
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_WITH_ID_NOT_FOUND, id)));
    }

    public Profile getEntityById(@NonNull Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public ProfileResponseDto getResponseById(@NonNull Long id) {
        return profileMapper.toResponseDto(getEntityById(id));
    }

    @Override
    public ProfileResponseDto getByEmail(@NonNull String email) {
        return profileRepository.findByEmailIgnoreCase(email)
                .map(profileMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException(String.format(PROFILE_WITH_EMAIL_NOT_FOUND, email)));
    }

    @Override
    public Page<ProfileResponseDto> getAll(@NonNull Pageable pageable) {
        return profileRepository.findAll(pageable)
                .map(profileMapper::toResponseDto);
    }

    private void emailAlreadyExist(String emailFromDto, String emailFromEntity) {
        Optional.ofNullable(emailFromDto)
                .flatMap(profileRepository::findByEmailIgnoreCase)
                .ifPresent(profile -> {
                    if (!emailFromDto.equals(emailFromEntity)) {
                        throw new BadRequestException(String.format(PROFILE_ALREADY_EXIST, emailFromDto));
                    }
                });
    }

    private void validateContentTypeUploadAvatar(String contentType) {
        if(!Objects.equals(contentType, ContentType.IMAGE_PNG.getMimeType()) && !Objects.equals(contentType, ContentType.IMAGE_JPEG.getMimeType())) {
            throw new BadRequestException(String.format(UPLOAD_AVATAR_INVALID_FORMAT, contentType));
        }
    }
}
