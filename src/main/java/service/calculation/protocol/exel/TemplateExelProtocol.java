package service.calculation.protocol.exel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.calculation.protocol.ProtocolWrap;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class TemplateExelProtocol implements ProtocolWrap {
    private static final Logger logger = LoggerFactory.getLogger(TemplateExelProtocol.class);

    private final HSSFWorkbook book;
    private final String folderPath;
    private final String fileName;

    /**
     * @param book exel form of protocol
     * @param folderName path to storage folder (if you want to save file in root of app keep it's empty or null)
     * @param fileName name of storage file (must be with extension = .xls)
     */
    public TemplateExelProtocol(@Nonnull HSSFWorkbook book, String folderPath, String fileName) {
        this.book = book;
        this.folderPath = folderPath;
        this.fileName = fileName;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean save() {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folder, fileName);
        try {
            file.createNewFile();
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                book.write(out);
                out.close();
                return true;
            } catch (IOException e) {
                if (Objects.nonNull(out)) out.close();
            }
        } catch (IOException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
        return false;
    }

    @Override
    public void print() {
        if (save()) {
            File folder = new File(folderPath);
            File file = new File(folder, fileName);
            Desktop desktop;
            if (Desktop.isDesktopSupported() && file.exists()) {
                desktop = Desktop.getDesktop();
                try {
                    if (desktop.isSupported(Desktop.Action.PRINT)) {
                        desktop.print(file);
                    } else {
                        String message = "Команда \"Друк\" за замовчуванням не зарегестрована в системі.";
                        logger.warn(message);
                    }
                } catch (Exception e) {
                    logger.warn(Messages.Log.EXCEPTION_THROWN, e);
                }
            }
        }
    }

    @Override
    public void open() {
        if (save()) {
            File folder = new File(folderPath);
            File file = new File(folder, fileName);
            Desktop desktop;
            if (Desktop.isDesktopSupported() && file.exists()) {
                desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (Exception ex) {
                    logger.warn(Messages.Log.EXCEPTION_THROWN, ex);
                }
            }
        }
    }
}
