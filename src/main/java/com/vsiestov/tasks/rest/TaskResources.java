package com.vsiestov.tasks.rest;

import com.vsiestov.platform.RestConstants;
import com.vsiestov.tasks.application.*;
import com.vsiestov.tasks.rest.dto.CreateTaskDTO;
import com.vsiestov.tasks.rest.dto.DeleteTaskDTO;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import com.vsiestov.tasks.rest.dto.UpdateTaskDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Tasks", description = "Tasks API")
@RestController
public class TaskResources {
    private final CreateTaskUseCase createTaskUseCase;
    private final RetrieveTasksUseCase retrieveTasksUseCase;
    private final RetrieveTaskByIdUseCase retrieveTaskByIdUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskResources(
        CreateTaskUseCase createTaskUseCase,
        RetrieveTasksUseCase retrieveTasksUseCase,
        RetrieveTaskByIdUseCase retrieveTaskByIdUseCase,
        UpdateTaskUseCase updateTaskUseCase,
        DeleteTaskUseCase deleteTaskUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.retrieveTasksUseCase = retrieveTasksUseCase;
        this.retrieveTaskByIdUseCase = retrieveTaskByIdUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @Operation(summary = "Get the list of user`s tasks")
    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskDTO>> getTasks(
        @RequestParam(value = RestConstants.PAGE, defaultValue = "0") int page,
        @RequestParam(value = RestConstants.SIZE, defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(retrieveTasksUseCase.execute(page, size), HttpStatus.OK);
    }

    @Operation(summary = "Get a task by id")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable long id) {
        return new ResponseEntity<>(retrieveTaskByIdUseCase.execute(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new task")
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO) {
        return new ResponseEntity<>(createTaskUseCase.execute(createTaskDTO), HttpStatus.OK);
    }

    @Operation(summary = "Update a task by id")
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable long id, @RequestBody @Valid UpdateTaskDTO updateTaskDTO) {
        return new ResponseEntity<>(updateTaskUseCase.execute(id, updateTaskDTO), HttpStatus.OK);
    }

    @Operation(summary = "Delete a task by id")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<DeleteTaskDTO> deleteTask(@PathVariable long id) {
        return new ResponseEntity<>(deleteTaskUseCase.execute(id), HttpStatus.OK);
    }
}
