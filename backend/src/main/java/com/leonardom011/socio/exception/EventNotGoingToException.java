package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class EventNotGoingToException extends ApiException {
    public EventNotGoingToException(Long eventId) {
        super(HttpStatus.BAD_REQUEST, "Event %d not going to.".formatted(eventId));
    }
}
