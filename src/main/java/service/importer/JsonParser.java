package service.importer;

import service.importer.model.Model;
import service.importer.model.ModelHolder;

import javax.annotation.Nullable;
import java.util.Map;

public interface JsonParser {
    @Nullable
    ModelHolder parse(String json, Model model);
    Map<String, String> parse(String json);
}
