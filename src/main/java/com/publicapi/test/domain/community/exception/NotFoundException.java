package com.publicapi.test.domain.community.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(){}

    public NotFoundException(String message) {
        super(message);
    }
}
