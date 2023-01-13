package service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class JacksonJsonObjectMapper implements JsonObjectMapper {
    private static final Logger logger = LoggerFactory.getLogger(JacksonJsonObjectMapper.class);

    private static JacksonJsonObjectMapper instance;

    private final ObjectMapper mapper;

    private JacksonJsonObjectMapper() {
        this.mapper = new ObjectMapper();
    }

    public static JacksonJsonObjectMapper getInstance() {
        if (instance == null) instance = new JacksonJsonObjectMapper();
        return instance;
    }

    @Override
    public <O> String objectToJson(@Nonnull O object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn("Exception was thrown!", e);
            return EMPTY;
        }
    }

    @Nullable
    @Override
    public <O> O JsonToObject(@Nonnull String json, Class<O> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.warn("Exception was thrown!", e);
            return null;
        }
    }
}
