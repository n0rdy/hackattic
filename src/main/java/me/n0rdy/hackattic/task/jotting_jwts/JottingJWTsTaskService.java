package me.n0rdy.hackattic.task.jotting_jwts;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

// https://hackattic.com/challenges/jotting_jwts
@Slf4j
@Service("jotting_jwts")
@RequiredArgsConstructor
public class JottingJWTsTaskService implements TaskService {
    private final HackatticClient hackatticClient;

    @Value("${hachattic.endpoints.jotting_jwts}")
    private String jwtProcessingEndpointUrl;
    private JwtData jwtData;

    @Override
    public HackatticServerResponse solve() {
        JwtSecret jwtSecret = hackatticClient.getTask(getTaskName(), JwtSecret.class);
        jwtData = new JwtData(jwtSecret.getJwtSecret());

        return hackatticClient.postSolution(getTaskName(), new AppUrl(jwtProcessingEndpointUrl));
    }

    public Optional<JwtSolution> handleJwt(String jwt) {
        DecodedJWT jwtContent = decryptJwt(jwt, jwtData.getSecret());

        // JWT is invalid
        if (jwtContent == null) {
            return Optional.empty();
        }

        if (jwtContent.getClaim("append").isNull()) {
            return Optional.of(new JwtSolution(jwtData.getSolution()));
        }

        jwtData.append(jwtContent.getClaim("append").asString());
        return Optional.empty();
    }

    @SneakyThrows
    private DecodedJWT decryptJwt(String jwt, String secret) {
        DecodedJWT decodedJWT = JWT.decode(jwt);

        if (!isValid(decodedJWT, secret)) {
            return null;
        }

        return decodedJWT;
    }

    private boolean isValid(DecodedJWT decodedJWT, String secret) {
        try {
            Algorithm algorithmHS = Algorithm.HMAC256(secret);
            algorithmHS.verify(decodedJWT);
        } catch (SignatureVerificationException e) {
            return false;
        }

        Date expiresAt = decodedJWT.getExpiresAt();
        if (expiresAt != null && expiresAt.before(new Date())) {
            return false;
        }

        Date notBefore = decodedJWT.getNotBefore();
        if (notBefore != null && notBefore.after(new Date())) {
            return false;
        }

        return true;
    }

    @Override
    public String getTaskName() {
        return "jotting_jwts";
    }

    private static class JwtData {
        private final String secret;
        private final StringBuilder solution;

        public JwtData(String secret) {
            this.secret = secret;
            this.solution = new StringBuilder();
        }

        public String getSecret() {
            return secret;
        }

        public void append(String data) {
            solution.append(data);
        }

        public String getSolution() {
            return solution.toString();
        }
    }
}
