package io.ussopm.ToDoListService.util;

import io.ussopm.ToDoListService.exception.AlreadyExistsException;
import io.ussopm.ToDoListService.exception.AlreadyTakenTaskException;
import io.ussopm.ToDoListService.exception.MarkingTaskException;
import io.ussopm.ToDoListService.exception.NoCustomerAssignedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;


@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "error 404");
        problemDetail.setProperty("errors", ex.getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(NoCustomerAssignedException.class)
    public ResponseEntity<ProblemDetail> handleNoCustomerAssignedException(NoCustomerAssignedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        ex.getErrorMessage()));
    }
    @ExceptionHandler(MarkingTaskException.class)
    public ResponseEntity<ProblemDetail> handleMarkingTaskException( MarkingTaskException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        ex.getErrorMessage()));
    }
    @ExceptionHandler(AlreadyTakenTaskException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyTakenTaskException(AlreadyTakenTaskException ex) {
        String taskName = ex.getTaskName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "You did not take task " + taskName + " or this task is already taken by another customer"));
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        ex.getMessage() + " not found"));
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyExistsException(AlreadyExistsException ex) {
        String taskName = ex.getTaskName();
        String username = ex.getCustomer();
        if (taskName != null && username != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "The task '" + taskName + "' is already assigned to " + username));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "The task is already exists"));
        }
    }

}
