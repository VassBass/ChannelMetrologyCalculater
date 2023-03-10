package service.importer;

import javax.annotation.Nullable;
import java.util.Map;

public interface JsonParser {
    @Nullable ModelHolder parse(String json, Model model);
    Map<String, String> parse(String json);
}
