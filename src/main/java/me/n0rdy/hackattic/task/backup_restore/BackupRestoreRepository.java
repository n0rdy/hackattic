package me.n0rdy.hackattic.task.backup_restore;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BackupRestoreRepository {
    private final JdbcTemplate jdbcTemplate;

    public Ssns getAliveSsns() {
        List<String> ssns = jdbcTemplate.query(
                "SELECT ssn FROM criminal_records WHERE status = 'alive'",
                (rs, rowNum) -> rs.getString("ssn")
        );

        return new Ssns(ssns);
    }

    @SneakyThrows
    public void restoreDbFromDump(String dumpFile) {
        String[] executeCmd = new String[]{"psql", "--username=postgres", "--file=" + dumpFile, "hackattic"};

        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);

        int processComplete = runtimeProcess.waitFor();

        if (processComplete != 0) {
            throw new RuntimeException("Restoring database was unsuccessful");
        }
    }

    public void cleanUpDb() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS criminal_records");
    }
}
