package io.ussopm.ToDoListService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoCustomerAssignedException extends RuntimeException{

//    private String taskName;
    private String errorMessage;
}
