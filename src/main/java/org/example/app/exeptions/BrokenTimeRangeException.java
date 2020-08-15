package org.example.app.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BrokenTimeRangeException extends RuntimeException {
    public BrokenTimeRangeException(String message) {
        super(message);
    }
}
