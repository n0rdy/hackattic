package me.n0rdy.hackattic.task.reading_qr;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.n0rdy.hackattic.config.HackatticClient;
import me.n0rdy.hackattic.exception.EmptyResponseBodyException;
import me.n0rdy.hackattic.exception.GeneralTaskException;
import me.n0rdy.hackattic.exception.TaskRestException;
import me.n0rdy.hackattic.model.HackatticServerResponse;
import me.n0rdy.hackattic.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildProblemUri;
import static me.n0rdy.hackattic.util.HackatticRemoteUrlBuilder.buildSolutionUri;

@Service("reading_qr")
@RequiredArgsConstructor
public class ReadingQRTaskService implements TaskService {
    private final HackatticClient hackatticClient;
    private final QRDecoder qrDecoder;

    @SneakyThrows
    @Override
    public HackatticServerResponse solve() {
        QR qr = hackatticClient.getTask(getTaskName(), QR.class);

        String qrCode = qrDecoder.decode(URI.create(qr.getImageUrl()).toURL())
                           .orElseThrow(() -> new GeneralTaskException("QR code couldn't be extracted from the image"));

        return hackatticClient.postSolution(getTaskName(), new Code(qrCode));
    }

    @Override
    public String getTaskName() {
        return "reading_qr";
    }
}
