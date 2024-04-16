package io.ussopm.ToDoListService.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewTaskPayload (@NotNull @Size(min = 3, max = 50, message = "Name have to be between 3 and 50")
                              String name,
                              @Size(max = 500, message = "Description should be less than 500")
                              String description){
}
