package com.nikitin.roadmaps.exception;

public class HttpResponseException extends RuntimeException {

    public HttpResponseException() {
        super();
    }

    public HttpResponseException(String message) {
        super(message);
    }
}
