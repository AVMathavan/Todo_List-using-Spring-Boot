package dev.avm.todolist.repository;


import dev.avm.todolist.models.Todo;
import dev.avm.todolist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser(User user);
}
