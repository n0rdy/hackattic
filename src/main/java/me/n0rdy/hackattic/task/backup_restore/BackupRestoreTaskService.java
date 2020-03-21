package me.n0rdy.hackattic.task.backup_restore;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.exception.EmptyResponseBodyException;
import me.n0rdy.hackattic.exception.TaskRestException;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildProblemUri;
import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildSolutionUri;

@Service("backup_restore")
@RequiredArgsConstructor
public class BackupRestoreTaskService implements TaskService {
    private static final String DUMP_FILE_NAME = "db";

    private final RestTemplate restTemplate;
    private final BackupRestoreRepository repository;

    @SneakyThrows
    @Override
    public HackatticServerResponse solve() {
        DbDump dbDump = restTemplate.getForEntity(buildProblemUri(getTaskName()), DbDump.class)
                                .getBody();

        if (dbDump == null) {
            throw new EmptyResponseBodyException();
        }

        byte[] decodedDbDump = Base64.getDecoder().decode(dbDump.getDump().getBytes());
        decompress(decodedDbDump, DUMP_FILE_NAME);

        repository.restoreDbFromDump(DUMP_FILE_NAME);
        Ssns aliveSsns = repository.getAliveSsns();

        ResponseEntity<HackatticServerResponse> response
                = restTemplate.postForEntity(buildSolutionUri(getTaskName()), aliveSsns, HackatticServerResponse.class);

        HackatticServerResponse hackatticResponse = response.getBody();

        if (hackatticResponse == null) {
            throw new EmptyResponseBodyException();
        }

        if (hackatticResponse.rejected()) {
            throw new TaskRestException(400, hackatticResponse.getRejected());
        }

        return hackatticResponse;
    }

    @SneakyThrows
    private void decompress(byte[] decodedDbDump, String dumpFileName) {
        byte[] buffer = new byte[1024];

        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(decodedDbDump));
             FileOutputStream out = new FileOutputStream(dumpFileName)) {

            int totalSize;
            while ((totalSize = gzip.read(buffer)) > 0) {
                out.write(buffer, 0, totalSize);
            }
        }
    }

    @Override
    public String getTaskName() {
        return "backup_restore";
    }

    @SneakyThrows
    @Override
    public void cleanUp() {
        if (Files.exists(Path.of(DUMP_FILE_NAME))) {
            Files.delete(Path.of(DUMP_FILE_NAME));
        }

        repository.cleanUpDb();
    }
}
