package com.nikitin.roadmaps.roadmapsbackendspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException {

    public BadRequest() {

    }

    public BadRequest(String message) {
        super(message);
    }
}
