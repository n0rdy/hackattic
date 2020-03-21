package me.n0rdy.hackattic.task.reading_qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QR {
    @JsonProperty("image_url")
    private String imageUrl;
}
