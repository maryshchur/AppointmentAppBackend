package org.example.app.exeptions.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException
           (RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link MethodArgumentNotValidException} exception.
     *
     * @param exception {@link MethodArgumentNotValidException}
     * @return ResponseEntity which contains error message
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<ValidationExceptionDto> collect =
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .map(ValidationExceptionDto::new)
                        .collect(Collectors.toList());
        collect.addAll(exception.getBindingResult().getGlobalErrors()
                .stream()
                .map(ValidationExceptionDto::new)
                .collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(collect);
    }

    private Map<String, String> generateErrorMessage(Exception exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", exception.getMessage());
        return errorResponse;
    }
}
