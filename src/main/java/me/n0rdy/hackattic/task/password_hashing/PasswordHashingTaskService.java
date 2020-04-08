package me.n0rdy.hackattic.task.password_hashing;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.bouncycastle.crypto.generators.SCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

//https://hackattic.com/challenges/password_hashing
@Service("password_hashing")
@RequiredArgsConstructor
public class PasswordHashingTaskService implements TaskService {
    private static final String PBKDF2_WITH_HMAC_SHA256_ALGORITHM = "PBKDF2WithHmacSHA256";

    private final HackatticClient hackatticClient;

    @Override
    public HackatticServerResponse solve() {
        PasswordHashingInput passwordHashingInput = hackatticClient.getTask(getTaskName(), PasswordHashingInput.class);

        String password = passwordHashingInput.getPassword();
        String salt = passwordHashingInput.getSalt();

        String sha256 = sha256(password);
        String hmac = hmac(password, salt);
        String pbkdf2 = pbkdf2(password, salt, passwordHashingInput.getPbkdf2());
        String scrypt = scrypt(password, salt, passwordHashingInput.getScrypt());

        PasswordHashingOutput passwordHashingOutput = new PasswordHashingOutput(sha256, hmac, pbkdf2, scrypt);

        return hackatticClient.postSolution(getTaskName(), passwordHashingOutput);
    }

    private String sha256(String password) {
        return DigestUtils.sha256Hex(password);
    }

    @SneakyThrows
    private String hmac(String password, String salt) {
        Mac hmacSHA256 = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, decodeBase64(salt));

        return Hex.encodeHexString(hmacSHA256.doFinal(password.getBytes(StandardCharsets.UTF_8)));
    }

    @SneakyThrows
    protected String pbkdf2(String password, String salt, Pbkdf2 pbkdf2) {
        KeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), decodeBase64(salt), pbkdf2.getRounds(), 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA256_ALGORITHM);

        return Hex.encodeHexString(factory.generateSecret(pbeKeySpec).getEncoded());
    }

    @SneakyThrows
    protected String scrypt(String password, String salt, Scrypt scrypt) {
        byte[] output = SCrypt.generate(
                password.getBytes(StandardCharsets.UTF_8), decodeBase64(salt), scrypt.getN(),
                scrypt.getBlockSize(), scrypt.getParallelization(), scrypt.getBuflen()
        );

        return Hex.encodeHexString(output);
    }

    private byte[] decodeBase64(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }

    @Override
    public String getTaskName() {
        return "password_hashing";
    }
}
