package com.nikitin.roadmaps.roadmapsbackendspring.controller;

import com.nikitin.roadmaps.roadmapsbackendspring.dto.request.ProfileRequestDto;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.response.ProfileResponseDto;
import com.nikitin.roadmaps.roadmapsbackendspring.service.ProfileService;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Create;
import com.nikitin.roadmaps.roadmapsbackendspring.validation.Patch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDto> create(@RequestBody @Validated(value = Create.class) ProfileRequestDto profileRequestDto) {
        var responseBody = profileService.create(profileRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> patch(@PathVariable (name = "id") Long id,
                                                     @RequestBody @Validated(value = Patch.class) ProfileRequestDto profileRequestDto) {
        var responseBody = profileService.patch(id, profileRequestDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getById(@PathVariable (name = "id") Long id) {
        var responseBody = profileService.getById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProfileResponseDto> getByEmail(@PathVariable (name = "email") String email) {
        var responseBody = profileService.getByEmail(email);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<Page<ProfileResponseDto>> getAll(@PageableDefault(
            sort = "name",
            direction = Sort.Direction.ASC,
            size = Integer.MAX_VALUE) Pageable pageable) {
        var responseBody = profileService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }
}
