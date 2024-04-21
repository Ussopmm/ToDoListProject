package io.ussopm.UserApp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlreadyExistsException extends RuntimeException {
    private String errorMessage;

}
