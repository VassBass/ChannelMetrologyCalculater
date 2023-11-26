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
public class Labels {
    private Labels(){}

    private static final Logger logger = LoggerFactory.getLogger(Labels.class);
    private static final Localization DEFAULT_LOCALIZATION = Localization.UA;

    private static final String ROOT_REGEX = "^\\_\\.";

    public static final String APPLICATION_NAME = "ChannelMetrologyCalculater";
    public static final String DOT = ".";
    public static final char dot = '.';
    public static final String DASH = "-";
    public static final String COMMA = ",";
    public static final String ZERRO = "0";
    public static final char zerro = '0';

    private static final Map<String, Map<String, String>> l;

    static {
        l = new HashMap<>();

        Localization localization = getCurrentLocalization();
        StringBuilder labelsFilePath = new StringBuilder("localization/labels/");
        switch (localization) {
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

            readLabels(properties);
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }

    private static Localization getCurrentLocalization() {
        return DEFAULT_LOCALIZATION;
    }

    private static void readLabels(Properties properties) {
        Map<String, String> rootLabels = l.containsKey(null) ? l.get(null) : new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (isRoot(key)) {
                String k = key.replaceAll(ROOT_REGEX, EMPTY);
                rootLabels.put(k, properties.getProperty(key));
            } else {
                int dotIndex = key.indexOf(DOT);
                String className = key.substring(0, dotIndex);
                String valueName = key.substring(++dotIndex);
                Map<String, String> classLabels = l.containsKey(className) ? l.get(className) : new HashMap<>();
                classLabels.put(valueName, properties.getProperty(key));
                l.put(className, classLabels);
            }
        }
        l.put(null, rootLabels);
    }

    private static boolean isRoot(String key) {
        Pattern pattern = Pattern.compile(ROOT_REGEX);
        return pattern.matcher(key).find();
    }

    public static Map<String, String> getRootLabels() {
        return l.get(null);
    }

    public static Map<String, String> getLabels(Class<?> c) {
        return l.get(c.getSimpleName());
    }
}
