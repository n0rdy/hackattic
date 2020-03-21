package me.n0rdy.hackattic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RestResponse {
    private String message;
    private String hint;
    @JsonProperty("error_message")
    private String errorMessage;

    public static RestResponse error(String errorMessage) {
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorMessage(errorMessage);
        return restResponse;
    }

    public static RestResponse success(String message, String hint) {
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage(message);
        restResponse.setHint(hint);
        return restResponse;
    }
}
