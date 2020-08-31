package me.n0rdy.hackattic.task.jotting_jwts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/jotting-jwts/jwts")
@RequiredArgsConstructor
public class JottingJwtsController {
    private final JottingJWTsTaskService jottingJWTsTaskService;

    @PostMapping
    public ResponseEntity handleJwt(@RequestBody String jwt) {
        Optional<JwtSolution> optionalJwtSolution = jottingJWTsTaskService.handleJwt(jwt);

        if (optionalJwtSolution.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(optionalJwtSolution.get());
    }
}
