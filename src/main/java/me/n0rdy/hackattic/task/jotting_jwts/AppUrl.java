package me.n0rdy.hackattic.task.jotting_jwts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AppUrl {
    @JsonProperty("app_url")
    String appUrl;
}
