package me.n0rdy.hackattic.task.password_hashing;

import lombok.Data;

@Data
public class Pbkdf2 {
    private String hash;
    private Integer rounds;
}
