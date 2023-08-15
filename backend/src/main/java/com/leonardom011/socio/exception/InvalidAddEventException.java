package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class InvalidAddEventException extends ApiException {

    public InvalidAddEventException() {
        super(HttpStatus.BAD_REQUEST, "cannot add event");
    }

}
