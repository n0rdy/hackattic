package me.n0rdy.hackattic.controller;

import lombok.RequiredArgsConstructor;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.model.RestResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private static final String TASK_NOT_EXISTS_ERROR_MESSAGE = "Task with the name [%s] doesn't exist";

    private final ApplicationContext context;

    @GetMapping("/{taskName}")
    public ResponseEntity<RestResponse> solveTask(@PathVariable String taskName) {
        if (!context.containsBean(taskName)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(buildRestError(taskName));
        }

        TaskService taskService = context.getBean(taskName, TaskService.class);

        HackatticServerResponse hackatticResponse = taskService.solve();
        taskService.cleanUp();

        return ResponseEntity.ok(RestResponse.success(hackatticResponse.getMessage(), hackatticResponse.getHint()));
    }

    private RestResponse buildRestError(String wrongTaskName) {
        String errorMessage = String.format(TASK_NOT_EXISTS_ERROR_MESSAGE, wrongTaskName);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorMessage(errorMessage);
        return restResponse;
    }
}


