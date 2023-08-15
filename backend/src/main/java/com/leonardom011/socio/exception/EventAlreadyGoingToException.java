package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class EventAlreadyGoingToException extends ApiException {
    public EventAlreadyGoingToException(Long eventId) {
        super(HttpStatus.BAD_REQUEST, "Event %d already going to".formatted(eventId));
    }
}
