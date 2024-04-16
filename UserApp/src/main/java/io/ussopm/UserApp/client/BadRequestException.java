package io.ussopm.UserApp.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException{
    private List<String> errors;

    public BadRequestException(List<String> errors) {
        this.errors = errors;
    }
}
