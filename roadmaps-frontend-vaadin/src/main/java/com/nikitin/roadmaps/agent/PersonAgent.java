package com.nikitin.roadmaps.agent;

import com.nikitin.roadmaps.dto.request.PersonRequestDto;
import com.nikitin.roadmaps.dto.response.PersonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class PersonAgent extends Agent {

    private PersonAgent() {
        super();
    }

    public ResponseEntity<PersonResponseDto> createPerson(PersonRequestDto personRequestDto) {
        return getRestTemplate().postForEntity(
                "/persons",
                personRequestDto,
                PersonResponseDto.class);
    }

    public static PersonAgent instance() {
        return new PersonAgent();
    }
}
