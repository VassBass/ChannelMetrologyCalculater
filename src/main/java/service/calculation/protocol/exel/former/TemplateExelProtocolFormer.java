package service.calculation.protocol.exel.former;

import model.dto.Calibrator;
import model.dto.Measurement;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.calculation.CalculationConfigHolder;
import service.calculation.protocol.Protocol;
import service.file_extractor.FileExtractor;
import service.file_extractor.ResourceFileExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class TemplateExelProtocolFormer implements ExelProtocolFormer {
    private static final Logger logger = LoggerFactory.getLogger(TemplateExelProtocolFormer.class);

    public static final int LINUX = 0;
    public static final int WINDOWS = 1;

    private static final String formsFolder = "forms";
    private static final String linuxFormTemperatureSuitableFileName = "linuxForm_temperature_good.xls";
    private static final String linuxFormTemperatureNotSuitableFileName = "linuxForm_temperature_bad.xls";
    private static final String windowsFormTemperatureSuitableFileName = "form_temperature_good.xls";
    private static final String windowsFormTemperatureNotSuitableFileName = "form_temperature_bad.xls";
    private static final String linuxFormPressureSuitableFileName = "linuxForm_pressure_good.xls";
    private static final String linuxFormPressureNotSuitableFileName = "linuxForm_pressure_bad.xls";
    private static final String windowsFormPressureSuitableFileName = "form_pressure_good.xls";
    private static final String windowsFormPressureNotSuitableFileName = "form_pressure_bad.xls";
    private static final String linuxFormConsumptionSuitableFileName = "linuxForm_consumption_good.xls";
    private static final String linuxFormConsumptionNotSuitableFileName = "linuxForm_consumption_bad.xls";
    private static final String windowsFormConsumptionSuitableFileName = "form_consumption_good.xls";
    private static final String windowsFormConsumptionNotSuitableFileName = "form_consumption_bad.xls";
    private static final String linuxFormConsumptionRosemountSuitableFileName = "linuxForm_consumption_rosemount_good.xls";
    private static final String linuxFormConsumptionRosemountNotSuitableFileName = "linuxForm_consumption_rosemount_bad.xls";
    private static final String windowsFormConsumptionRosemountSuitableFileName = "form_consumption_rosemount_good.xls";
    private static final String windowsFormConsumptionRosemountNotSuitableFileName = "form_consumption_rosemount_bad.xls";

    private final CalculationConfigHolder configHolder;
    private final int os;
    private HSSFWorkbook book;

    public TemplateExelProtocolFormer(CalculationConfigHolder configHolder, int os) {
        this.configHolder = configHolder;
        this.os = os;
    }

    @Override
    public HSSFWorkbook format(Protocol protocol) {
        book = createBook(protocol);

        return null;
    }

    private HSSFWorkbook createBook(Protocol protocol) {
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();
        String measurement = protocol.getChannel().getMeasurementName();
        File template = null;
        if (os == LINUX) {
            if (suitable) {
                if (measurement.equals(Measurement.TEMPERATURE)) template = new File(formsFolder, linuxFormTemperatureSuitableFileName);
                if (measurement.equals(Measurement.PRESSURE)) template = new File(formsFolder, linuxFormPressureSuitableFileName);
                if (measurement.equals(Measurement.CONSUMPTION)) {
                    Calibrator calibrator = protocol.getCalibrator();
                    if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)) {
                        template = new File(formsFolder, linuxFormConsumptionRosemountSuitableFileName);
                    } else {
                        template = new File(formsFolder, linuxFormConsumptionSuitableFileName);
                    }
                }
            } else {
                if (measurement.equals(Measurement.TEMPERATURE)) template = new File(formsFolder, linuxFormTemperatureNotSuitableFileName);
                if (measurement.equals(Measurement.PRESSURE)) template = new File(formsFolder, linuxFormPressureNotSuitableFileName);
                if (measurement.equals(Measurement.CONSUMPTION)) {
                    Calibrator calibrator = protocol.getCalibrator();
                    if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)) {
                        template = new File(formsFolder, linuxFormConsumptionRosemountNotSuitableFileName);
                    } else {
                        template = new File(formsFolder, linuxFormConsumptionNotSuitableFileName);
                    }
                }
            }
        }

        if (os == WINDOWS) {
            if (suitable) {
                if (measurement.equals(Measurement.TEMPERATURE)) template = new File(formsFolder, windowsFormTemperatureSuitableFileName);
                if (measurement.equals(Measurement.PRESSURE)) template = new File(formsFolder, windowsFormPressureSuitableFileName);
                if (measurement.equals(Measurement.CONSUMPTION)) {
                    Calibrator calibrator = protocol.getCalibrator();
                    if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)) {
                        template = new File(formsFolder, windowsFormConsumptionRosemountSuitableFileName);
                    } else {
                        template = new File(formsFolder, windowsFormConsumptionSuitableFileName);
                    }
                }
            } else {
                if (measurement.equals(Measurement.TEMPERATURE)) template = new File(formsFolder, windowsFormTemperatureNotSuitableFileName);
                if (measurement.equals(Measurement.PRESSURE)) template = new File(formsFolder, windowsFormPressureNotSuitableFileName);
                if (measurement.equals(Measurement.CONSUMPTION)) {
                    Calibrator calibrator = protocol.getCalibrator();
                    if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)) {
                        template = new File(formsFolder, windowsFormConsumptionRosemountNotSuitableFileName);
                    } else {
                        template = new File(formsFolder, windowsFormConsumptionNotSuitableFileName);
                    }
                }
            }
        }

        if (Objects.nonNull(template) && !template.exists()) {
            FileExtractor fileExtractor = ResourceFileExtractor.getInstance();
            String sourcePath = String.format("%s/%s", formsFolder, template.getName());
            fileExtractor.extract(sourcePath, template.getAbsolutePath());

            try (POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(template))) {
                return new HSSFWorkbook(fs);
            } catch (IOException e) {
                logger.warn("Exception was thrown", e);
            }
        }

        return null;
    }

    private HSSFCell cell(int row, int column){
        HSSFSheet sheet = this.book.getSheetAt(0);
        HSSFRow Row = sheet.getRow(row);
        return Row.getCell(column);
    }
}
