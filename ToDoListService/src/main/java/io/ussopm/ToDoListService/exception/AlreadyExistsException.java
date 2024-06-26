package io.ussopm.ToDoListService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlreadyExistsException extends RuntimeException {
    private String taskName;
    private String customer;

}
