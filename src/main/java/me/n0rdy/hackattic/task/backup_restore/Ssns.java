package me.n0rdy.hackattic.task.backup_restore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class Ssns {
    @JsonProperty("alive_ssns")
    private List<String> ssns;
}
