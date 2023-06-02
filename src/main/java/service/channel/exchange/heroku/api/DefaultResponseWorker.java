package service.channel.exchange.heroku.api;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DefaultResponseWorker implements ResponseWorker {
    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseWorker.class);

    private static final String REQUEST_FORMAT = "http://cmc-exchanger.herokuapp.com/get_content/%s";

    @Nullable
    @Override
    public String getContent(int id) {
        if (id < 1_000 || id >= 10_000) {
            logger.warn("Id must be 1000 <= id < 10'000");
            return null;
        }

        String request = String.format(REQUEST_FORMAT, id);
        try (InputStream in = new URL(request).openConnection().getInputStream()) {
            return new String(IOUtils.toByteArray(in), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.warn("Exception was thrown!", e);
            return null;
        }
    }
}
