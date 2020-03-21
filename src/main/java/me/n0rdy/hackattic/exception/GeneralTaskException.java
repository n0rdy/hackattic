package me.n0rdy.hackattic.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class GeneralTaskException extends RuntimeException {
    private String message;
}
