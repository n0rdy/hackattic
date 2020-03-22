package me.n0rdy.hackattic.task.help_me_unpack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class DecodedData {
    @JsonProperty("int")
    private int intValue;
    @JsonProperty("uint")
    private long uintValue;
    @JsonProperty("short")
    private short shortValue;
    @JsonProperty("float")
    private double floatValue;
    @JsonProperty("double")
    private double doubleValue;
    @JsonProperty("big_endian_double")
    private double bidDoubleValue;
}
