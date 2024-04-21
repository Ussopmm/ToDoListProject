package io.ussopm.ToDoListService.exception;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlreadyTakenTaskException extends RuntimeException{

    private String taskName;
    private String username;

    public AlreadyTakenTaskException(String taskName) {
        this.taskName = taskName;
    }

}
