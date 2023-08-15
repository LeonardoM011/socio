package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class EventAlreadyInterestedToException extends ApiException {
    public EventAlreadyInterestedToException(Long eventId) {
        super(HttpStatus.BAD_REQUEST, "Event %d already interested in".formatted(eventId));
    }
}
