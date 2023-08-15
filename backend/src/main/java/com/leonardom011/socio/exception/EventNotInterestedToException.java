package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class EventNotInterestedToException extends ApiException {
    public EventNotInterestedToException(Long eventId) {
        super(HttpStatus.BAD_REQUEST, "Event %d not interested in.".formatted(eventId));
    }
}
