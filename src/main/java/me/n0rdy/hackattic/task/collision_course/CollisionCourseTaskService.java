package me.n0rdy.hackattic.task.collision_course;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Base64;

// https://hackattic.com/challenges/collision_course
@Service("collision_course")
@RequiredArgsConstructor
public class CollisionCourseTaskService implements TaskService {
    private static final String FIRST_FILE_NAME = "firstFile.txt";
    private static final String SECOND_FILE_NAME = "secondFile.txt";

    // blocks taken from https://www.mathstat.dal.ca/~selinger/md5collision/, that lead to MD5 collision
    private static final String FIRST_FILE_BLOCK = "d131dd02c5e6eec4693d9a0698aff95c2fcab58712467eab4004583eb8fb7f89" +
                                                           "55ad340609f4b30283e488832571415a085125e8f7cdc99fd91dbdf280373c5b" +
                                                           "d8823e3156348f5bae6dacd436c919c6dd53e2b487da03fd02396306d248cda0" +
                                                           "e99f33420f577ee8ce54b67080a80d1ec69821bcb6a8839396f9652b6ff72a70";
    private static final String SECOND_FILE_BLOCK = "d131dd02c5e6eec4693d9a0698aff95c2fcab50712467eab4004583eb8fb7f89" +
                                                            "55ad340609f4b30283e4888325f1415a085125e8f7cdc99fd91dbd7280373c5b" +
                                                            "d8823e3156348f5bae6dacd436c919c6dd53e23487da03fd02396306d248cda0" +
                                                            "e99f33420f577ee8ce54b67080280d1ec69821bcb6a8839396f965ab6ff72a70";

    private final HackatticClient hackatticClient;

    @SneakyThrows
    @Override
    public HackatticServerResponse solve() {
        CommonString commonString = hackatticClient.getTask(getTaskName(), CommonString.class);
        createFilesWithMd5CollisionContainingString(commonString.getInclude());

        Collision collision = new Collision(
                Arrays.asList(encodeFileToBase64Binary(FIRST_FILE_NAME), encodeFileToBase64Binary(SECOND_FILE_NAME))
        );

        return hackatticClient.postSolution(getTaskName(), collision);
    }

    @SneakyThrows
    private void createFilesWithMd5CollisionContainingString(String stringToInclude) {
        Files.createFile(Path.of(FIRST_FILE_NAME));
        Files.createFile(Path.of(SECOND_FILE_NAME));

        Files.write(Path.of(FIRST_FILE_NAME), HexUtils.fromHexString(FIRST_FILE_BLOCK));
        Files.write(Path.of(FIRST_FILE_NAME), stringToInclude.getBytes(), StandardOpenOption.APPEND);

        Files.write(Path.of(SECOND_FILE_NAME), HexUtils.fromHexString(SECOND_FILE_BLOCK));
        Files.write(Path.of(SECOND_FILE_NAME), stringToInclude.getBytes(), StandardOpenOption.APPEND);
    }

    @SneakyThrows
    private static String encodeFileToBase64Binary(String fileName) {
        byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(new File(fileName)));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    @Override
    public String getTaskName() {
        return "collision_course";
    }

    @SneakyThrows
    @Override
    public void cleanUp() {
        Files.delete(Path.of(FIRST_FILE_NAME));
        Files.delete(Path.of(SECOND_FILE_NAME));
    }
}
