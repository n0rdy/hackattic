package me.n0rdy.hackattic.task.backup_restore;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.client.HackatticClient;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Service("backup_restore")
@RequiredArgsConstructor
public class BackupRestoreTaskService implements TaskService {
    private static final String DUMP_FILE_NAME = "db";

    private final HackatticClient hackatticClient;
    private final BackupRestoreRepository repository;

    @SneakyThrows
    @Override
    public HackatticServerResponse solve() {
        DbDump dbDump = hackatticClient.getTask(getTaskName(), DbDump.class);

        byte[] decodedDbDump = Base64.getDecoder().decode(dbDump.getDump().getBytes());
        decompress(decodedDbDump, DUMP_FILE_NAME);

        repository.restoreDbFromDump(DUMP_FILE_NAME);
        Ssns aliveSsns = repository.getAliveSsns();

        return hackatticClient.postSolution(getTaskName(), aliveSsns);
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
