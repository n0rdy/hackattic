package me.n0rdy.hackattic.task.help_me_unpack;

import lombok.RequiredArgsConstructor;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

// https://hackattic.com/challenges/help_me_unpack
@Service("help_me_unpack")
@RequiredArgsConstructor
public class HelpMeUnpackTaskService implements TaskService {
    private final HackatticClient hackatticClient;

    @Override
    public HackatticServerResponse solve() {
        EncodedBytes encodedBytes = hackatticClient.getTask(getTaskName(), EncodedBytes.class);

        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);

        int currentIndex = 0;

        int intValue = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt(currentIndex);
        currentIndex += Integer.BYTES;

        long uintValue = getUnsignedInt(byteBuffer.getInt(currentIndex));
        currentIndex += Integer.BYTES;

        short shortValue = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getShort(currentIndex);
        currentIndex += Integer.BYTES;

        double floatValue = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getFloat(currentIndex);
        currentIndex += Float.BYTES;

        double doubleValue = byteBuffer.getDouble(currentIndex);
        currentIndex += Double.BYTES;

        double bigDoubleValue = byteBuffer.order(ByteOrder.BIG_ENDIAN).getDouble(currentIndex);

        DecodedData decodedData = new DecodedData(intValue, uintValue, shortValue, floatValue, doubleValue, bigDoubleValue);

        return hackatticClient.postSolution(getTaskName(), decodedData);
    }

    private long getUnsignedInt(int intValue) {
        return intValue & 0x00000000FFFFFFFFL;
    }

    @Override
    public String getTaskName() {
        return "help_me_unpack";
    }
}
