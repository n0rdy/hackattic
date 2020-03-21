package me.n0rdy.hackattic.client;

import lombok.RequiredArgsConstructor;
import me.n0rdy.hackattic.exception.EmptyResponseBodyException;
import me.n0rdy.hackattic.exception.TaskRestException;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildProblemUri;
import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildSolutionUri;

@Component
@RequiredArgsConstructor
public class HackatticClient {
    private final RestTemplate restTemplate;

    public <T> T getTask(String taskName, Class<T> taskClass) {
        T task = restTemplate.getForEntity(buildProblemUri(taskName), taskClass)
                         .getBody();

        if (task == null) {
            throw new EmptyResponseBodyException();
        }

        return task;
    }

    public <T> HackatticServerResponse postSolution(String taskName, T solution) {
        HackatticServerResponse hackatticResponse
                = restTemplate.postForEntity(buildSolutionUri(taskName), solution, HackatticServerResponse.class)
                          .getBody();

        if (hackatticResponse == null) {
            throw new EmptyResponseBodyException();
        }

        if (hackatticResponse.rejected()) {
            throw new TaskRestException(400, hackatticResponse.getRejected());
        }

        return hackatticResponse;
    }
}
