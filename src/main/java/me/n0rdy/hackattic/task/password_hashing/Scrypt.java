package me.n0rdy.hackattic.task.password_hashing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Scrypt {
    @JsonProperty("N")
    private Integer n;
    @JsonProperty("p")
    private Integer parallelization;
    @JsonProperty("r")
    private Integer blockSize;
    private Integer buflen;
    @JsonProperty("_control")
    private String control;
}
