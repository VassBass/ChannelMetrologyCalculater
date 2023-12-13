package service.calculation.protocol.exel.template;

import localization.Messages;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.file_extractor.FileExtractor;
import service.file_extractor.ResourceFileExtractor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ExelTemplateHolder {
    private static final Logger logger = LoggerFactory.getLogger(ExelTemplateHolder.class);
    /**
     * Key = measurement name, value = path of template in resources
     */
    protected final Map<String, String> templates = new HashMap<>();

    public ExelTemplateHolder() {
        initialization();
    }

    /**
     * To use template put file with template to resources folder,
     * then registrar path to template in {@link #templates} with this method
     * Example:
     * if your template file location = src/main/resources/forms/your_template.xls
     * {@code buffer.put("your_measurement_name", "forms/your_template.xls")}
     */
    protected abstract void initialization();

    public HSSFWorkbook getTemplateFor(@Nonnull String measurementName) {
        File template = getTemplateFileFor(measurementName);
        if (Objects.isNull(template)) return null;

        try (FileInputStream fs = new FileInputStream(template)) {
            return new HSSFWorkbook(fs);
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return null;
        }
    }

    private File getTemplateFileFor(@Nonnull String measurementName) {
        String templatePath = templates.get(measurementName);
        if (Objects.isNull(templatePath)) {
            logger.warn(String.format("Template for measurement = '%s' was not found", measurementName));
            return null;
        }
        File template = new File(templatePath);

        if (!template.exists()) {
            FileExtractor fileExtractor = ResourceFileExtractor.getInstance();
            fileExtractor.extract(templatePath, template.getAbsolutePath());
        }
        return template;
    }
}
