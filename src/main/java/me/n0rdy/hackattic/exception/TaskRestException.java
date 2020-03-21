package me.n0rdy.hackattic.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TaskRestException extends RuntimeException {
    private int httpStatusCode;
    private String message;
}
