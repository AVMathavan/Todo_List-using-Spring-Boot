package dev.avm.todolist.controller;

import dev.avm.todolist.models.Todo;
import dev.avm.todolist.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@Slf4j
public class TodoController {

    @Autowired
    private TodoService todoService;

    //Path Variable
    @GetMapping("/{id}")
    ResponseEntity<Todo> getTodoById(@PathVariable int id){
        try {
            Todo createdTodo = todoService.getTodoById(id);
            return new ResponseEntity<>(createdTodo, HttpStatus.OK);
        } catch (RuntimeException exception) {
            log.info("Error");
            log.warn("");
            log.error("", exception);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/page")
     ResponseEntity<Page<Todo>> getTodoPaged(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(todoService.getAllTodoPages(page, size), HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @PutMapping
    ResponseEntity<Todo> updateTodoById(@RequestBody Todo todo) {
        return new ResponseEntity<>(todoService.updateTodo(todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
     void deleteTodoById(@PathVariable int id) {
        todoService.deleteTodoById(id);
    }

    @GetMapping
    ResponseEntity<List<Todo>> getTodos(){
        return new ResponseEntity<>(todoService.getTodosByLoggedInUser(), HttpStatus.OK);
    }


}
