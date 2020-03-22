package me.n0rdy.hackattic.task.mini_miner;

import lombok.Data;

import java.util.List;

@Data
public class MinerData {
    private int difficulty;
    private Block block;

    @Data
    static class Block {
        // Object as list contains both String and int
        private List<List<Object>> data;
        private Integer nonce;
    }
}
