package me.n0rdy.hackattic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class HackatticServerResponse {
    private String passed;
    private String rejected;
    private String message;
    private String hint;

    @JsonIgnore
    public boolean rejected() {
        return rejected != null;
    }
}
