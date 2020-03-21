package me.n0rdy.hackattic.config;

import lombok.SneakyThrows;
import me.n0rdy.hackattic.exception.TaskRestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class TaskRestErrorHandler extends DefaultResponseErrorHandler {

    @Override
    @SneakyThrows
    public void handleError(ClientHttpResponse response) {
        HttpStatus statusCode = response.getStatusCode();
        throw new TaskRestException(statusCode.value(), statusCode.getReasonPhrase());
    }
}
