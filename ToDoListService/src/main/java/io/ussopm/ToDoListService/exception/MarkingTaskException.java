package io.ussopm.ToDoListService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MarkingTaskException extends RuntimeException{

    private String errorMessage;
}
