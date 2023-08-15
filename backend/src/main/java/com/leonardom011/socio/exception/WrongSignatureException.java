package com.leonardom011.socio.exception;

import org.springframework.http.HttpStatus;

public class WrongSignatureException extends ApiException {

    public WrongSignatureException() {
        super(HttpStatus.UNAUTHORIZED, "Wrong signature");
    }
}
