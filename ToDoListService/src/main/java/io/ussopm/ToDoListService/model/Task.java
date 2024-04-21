package io.ussopm.ToDoListService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Name should be not empty")
    @Size(min = 3, max = 50, message = "Name have to be between 3 and 50")
    private String name;

    @Column(name = "c_description")
    @Size(max = 500, message = "Description should be less than 500")
    private String description;

    @Column(name = "c_task_status")
    @NotNull
    private Boolean taskStatus;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;

    public Task() {
    }

    public Task(String name, String description, Boolean taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }
}
