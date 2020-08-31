package me.n0rdy.hackattic.task.jotting_jwts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtSecret {
    @JsonProperty("jwt_secret")
    private String jwtSecret;
}
