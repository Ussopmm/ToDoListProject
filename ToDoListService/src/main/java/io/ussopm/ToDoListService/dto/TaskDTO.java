package io.ussopm.ToDoListService.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private int id;

    @NotNull
    @NotBlank(message = "Name should be not empty")
    @Size(min = 3, max = 50, message = "Name have to be between 3 and 50")
    private String name;

    @Size(max = 500, message = "Description should be less than 500")
    private String description;

    @NotNull
    private Boolean taskStatus;

}
