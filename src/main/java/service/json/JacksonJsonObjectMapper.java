package service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import localization.Labels;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class JacksonJsonObjectMapper implements JsonObjectMapper {
    private static final Logger logger = LoggerFactory.getLogger(JacksonJsonObjectMapper.class);

    private static final String CURLY_BRACKETS_REGEX = "^\\{|\\}$";
    private static final String NEW_STRING_REGEX = "(\\n\\s\\s)|(\\n)";

    private static JacksonJsonObjectMapper instance;

    private final ObjectMapper mapper;

    private JacksonJsonObjectMapper() {
        this.mapper = new ObjectMapper();
    }

    public static JacksonJsonObjectMapper getInstance() {
        if (instance == null) instance = new JacksonJsonObjectMapper();
        return instance;
    }

    /**
     * Don't use for {@code Map<Double, Double>} because transform keys to Strings. For that case use {@link #doubleMapToJson(Map)}
     * @param object to serialize
     * @param <O> type of object to serialize.
     *           Don't use :    {@code Map<Double, SomeValue>}
     *                          {@code Map<Integer, SomeValue>}
     *                          {@code Map<Long, SomeValue>}
     *                          {@code Map<Float, SomeValue>}
     * @return json string of object
     * Example:
     * {
     *     "name" : "Name",
     *     "number" : 50.0,
     *     "boolean" : "true"
     * }
     */
    @Override
    public <O> String objectToJson(@Nonnull O object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return EMPTY;
        }
    }

    /**
     * Don't use for {@code Map<Double, Double>} because returns null.
     * @param json string of serialized object
     * @param clazz class of serialized object
     * @param <O> type of serialized object
     * @return serialized object
     */
    @Nullable
    @Override
    public <O> O jsonToObject(@Nonnull String json, Class<O> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return null;
        }
    }

    @Override
    public Map<Double, Double> jsonToDoubleMap(@Nonnull String json) {
        Map<Double, Double> result = new HashMap<>();
        String[] fields = json.replaceAll(CURLY_BRACKETS_REGEX, EMPTY).replaceAll(NEW_STRING_REGEX, EMPTY).split(Labels.COMMA);
        for (String f : fields) {
            String[] d = f.split(Labels.COLON);

            if (d.length < 2) continue;
            if (!StringHelper.isDouble(d[0])) continue;
            if (!StringHelper.isDouble(d[1])) continue;

            result.put(Double.parseDouble(d[0]), Double.parseDouble(d[1]));
        }
        return result;
    }

    @Override
    public String doubleMapToJson(Map<Double, Double> doubleMap) {
        StringBuilder builder = new StringBuilder("{");
        for (Map.Entry<Double, Double> entry : doubleMap.entrySet()) {
            builder.append(String.format("\n %s : %s,", entry.getKey(), entry.getValue()));
        }
        builder.setCharAt(builder.length() - 1, '\n');
        builder.append("}");
        return builder.toString();
    }
}
