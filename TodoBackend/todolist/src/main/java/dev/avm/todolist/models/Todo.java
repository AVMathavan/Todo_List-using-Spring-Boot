package dev.avm.todolist.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    int id;
    @NotNull
    @NotBlank
    String title;
    String description;
    private boolean completed;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
