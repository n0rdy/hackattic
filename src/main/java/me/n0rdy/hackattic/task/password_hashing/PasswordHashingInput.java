package me.n0rdy.hackattic.task.password_hashing;

import lombok.Data;

@Data
public class PasswordHashingInput {
    private String password;
    private String salt;
    private Pbkdf2 pbkdf2;
    private Scrypt scrypt;
}
