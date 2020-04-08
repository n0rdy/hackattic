package me.n0rdy.hackattic.task.password_hashing;

import lombok.Value;

@Value
public class PasswordHashingOutput {
    private String sha256;
    private String hmac;
    private String pbkdf2;
    private String scrypt;
}
