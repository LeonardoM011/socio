package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class CouldNotRegisterException extends ApiException {

    public CouldNotRegisterException(String msg) {
        super(HttpStatus.FORBIDDEN, msg);
    }
}
