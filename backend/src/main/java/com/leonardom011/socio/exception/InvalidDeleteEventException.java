package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class InvalidDeleteEventException extends ApiException {

    public InvalidDeleteEventException(Long eventId) {
        super(HttpStatus.NOT_FOUND, "Cannot delete event: event " + eventId + " not found");
    }
}
