package com.yatseniuk.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongActionException extends RuntimeException {

    public WrongActionException(String message) {
        super(message);
    }
}