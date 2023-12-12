package localization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("all")
public class Messages {
    private Messages(){}

    private static final Logger logger = LoggerFactory.getLogger(Messages.class);

    private static final String ROOT_REGEX = "^\\_\\.";

    private static final Map<String, Map<String, String>> m;

    static {
        m = new HashMap<>();

        StringBuilder labelsFilePath = new StringBuilder("localization/messages/");
        switch (Localization.getCurrentLocalization()) {
            case UA:
                labelsFilePath.append("ukr");
                break;
        }
        labelsFilePath.append(".properties");

        init(labelsFilePath.toString());
    }

    private static void init(String labelsFilePath) {
        try (InputStream in = Labels.class.getClassLoader().getResourceAsStream(labelsFilePath)) {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));

            readMessages(properties);
        } catch (IOException e) {
            logger.warn(Log.EXCEPTION_THROWN, e);
        }
    }

    private static void readMessages(Properties properties) {
        Map<String, String> rootMessages = m.containsKey(null) ? m.get(null) : new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (isRoot(key)) {
                String k = key.replaceAll(ROOT_REGEX, EMPTY);
                rootMessages.put(k, properties.getProperty(key));
            } else {
                int dotIndex = key.indexOf(Labels.DOT);
                String className = key.substring(0, dotIndex);
                String valueName = key.substring(++dotIndex);
                Map<String, String> classMessages = m.containsKey(className) ? m.get(className) : new HashMap<>();
                classMessages.put(valueName, properties.getProperty(key));
                m.put(className, classMessages);
            }
        }
        m.put(null, rootMessages);
    }

    private static boolean isRoot(String key) {
        Pattern pattern = Pattern.compile(ROOT_REGEX);
        return pattern.matcher(key).find();
    }

    public static Map<String, String> getRootMessages() {
        return m.get(null);
    }

    public static Map<String, String> getMessages(Class<?> c) {
        return m.get(c.getSimpleName());
    }

    public static class Log {
        private static final String MISSING_IMPL = "Can't find implementation for %s";

        public static final String INIT_SUCCESS = "Initialization completed successfully";
        public static final String EXCEPTION_THROWN = "Exception was thrown!";
        public static final String SERVICE_RUNNING = "Service is running";
        public static final String MISSING_UI_MANAGER_ERROR = "Before use context you must register manager!";
        public static final String PRINT_COMMAND_NOT_REGISTRATED = "The Print command is not registered in the system by default.";

        public static String missingImplementation(Class<?> c) {
            return String.format(MISSING_IMPL, c.getName());
        }
    }
}
