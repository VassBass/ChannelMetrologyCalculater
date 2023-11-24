package localization;

import localization.message.Messages;
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

public class Labels {
    private Labels(){}

    private static final Logger logger = LoggerFactory.getLogger(Labels.class);
    private static final Localization DEFAULT_LOCALIZATION = Localization.UA;

    private static final String ROOT_LABEL_REGEX = "^\\_\\.";

    public static final String DOT = ".";
    public static final String DASH = "-";
    public static final String COMMA = ",";

    private static final Map<String, Map<String, String>> labels;

    static {
        labels = new HashMap<>();

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

            readRootLabels(properties);
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }

    private static Localization getCurrentLocalization() {
        return DEFAULT_LOCALIZATION;
    }

    private static void readRootLabels(Properties properties) {
        Map<String, String> rootLabels = labels.containsKey(null) ? labels.get(null) : new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (isRootLabel(key)) {
                String k = key.replaceAll(ROOT_LABEL_REGEX, "");
                rootLabels.put(k, properties.getProperty(key));
            } else {
                int dotIndex = key.indexOf(DOT);
                String className = key.substring(0, dotIndex);
                String valueName = key.substring(++dotIndex);
                Map<String, String> classLabels = labels.containsKey(className) ? labels.get(className) : new HashMap<>();
                classLabels.put(valueName, properties.getProperty(key));
                labels.put(className, classLabels);
            }
        }
        labels.put(null, rootLabels);
    }

    private static boolean isRootLabel(String key) {
        Pattern pattern = Pattern.compile(ROOT_LABEL_REGEX);
        return pattern.matcher(key).find();
    }

    public static void main(String[] args) {
        for (Map.Entry<String, String> e : labels.get("SomeClass").entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
        }
        System.out.println(Labels.class.getSimpleName() + " Done!");
    }
}
