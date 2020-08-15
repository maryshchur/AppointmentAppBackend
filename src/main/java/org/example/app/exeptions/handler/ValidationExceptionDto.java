package org.example.app.exeptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidationExceptionDto implements Serializable {
    private String name;
    private String message;

    public ValidationExceptionDto(FieldError error) {
        this.name = error.getField();
        this.message = error.getDefaultMessage();
    }
    public ValidationExceptionDto(ObjectError error) {
        this.message = error.getDefaultMessage();
    }


}
