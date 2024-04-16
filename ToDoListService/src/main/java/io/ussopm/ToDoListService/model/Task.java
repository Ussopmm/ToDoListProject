package io.ussopm.ToDoListService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "t_tasks")
@Data
public class Task{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c_task_name")
    @NotNull
    @Size(min = 3, max = 50, message = "Name have to be between 3 and 50")
    private String name;

    @Column(name = "c_description")
    @Size(max = 500, message = "Description should be less than 500")
    private String description;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
