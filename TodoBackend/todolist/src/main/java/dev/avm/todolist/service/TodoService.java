package dev.avm.todolist.service;


import dev.avm.todolist.models.Todo;
import dev.avm.todolist.models.User;
import dev.avm.todolist.repository.TodoRepository;
import dev.avm.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    //Autowire
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Todo> getAllTodoPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    }

    public void deleteTodo(Todo todo){
        todoRepository.delete(todo);
    }

    public List<Todo> getTodosByLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // assumes email is username
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return todoRepository.findByUser(user);
    }



    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Todo createTodo(Todo todo) {
        User user = getCurrentUser();
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public List<Todo> getTodos() {
        User user = getCurrentUser();
        return todoRepository.findByUser(user);
    }

    public Todo getTodoById(Integer id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        User currentUser = getCurrentUser();
        if (!todo.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized access to todo");
        }

        return todo;
    }


    public Todo updateTodo(Todo updatedTodo) {
        Todo existingTodo = getTodoById(updatedTodo.getId()); // checks ownership

        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setCompleted(updatedTodo.isCompleted());

        return todoRepository.save(existingTodo);
    }



    public void deleteTodoById(Integer id) {
        Todo todo = getTodoById(id); // includes ownership check
        todoRepository.delete(todo);
    }


}
