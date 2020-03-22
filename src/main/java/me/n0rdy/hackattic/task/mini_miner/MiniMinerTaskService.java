package me.n0rdy.hackattic.task.mini_miner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import me.n0rdy.hackattic.task.mini_miner.MinerData.Block;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.BitSet;

// https://hackattic.com/challenges/mini_miner
@Service("mini_miner")
@RequiredArgsConstructor
public class MiniMinerTaskService implements TaskService {
    private final HackatticClient hackatticClient;

    @SneakyThrows
    @Override
    public HackatticServerResponse solve() {
        MinerData minerData = hackatticClient.getTask(getTaskName(), MinerData.class);

        int nonce = compute(minerData.getBlock(), minerData.getDifficulty());

        return hackatticClient.postSolution(getTaskName(), new Nonce(nonce));
    }

    @SneakyThrows
    private int compute(Block block, int difficulty) {
        int nonce = 0;

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] hash;

        do {
            block.setNonce(++nonce);
            String blockString = objectMapper.writeValueAsString(block);
            hash = sha256.digest(blockString.getBytes());
        } while (!startsWithZerosBits(hash, difficulty));

        return nonce;
    }

    @SneakyThrows
    private boolean startsWithZerosBits(byte[] hash, int numberOfZerosBits) {
        BitSet bitSet = BitSet.valueOf(new byte[]{hash[0], hash[1]});

        for (int i = 0; i < numberOfZerosBits; i++) {
            if (bitSet.get(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getTaskName() {
        return "mini_miner";
    }
}
