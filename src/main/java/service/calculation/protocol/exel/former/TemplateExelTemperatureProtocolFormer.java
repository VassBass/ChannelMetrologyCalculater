package service.calculation.protocol.exel.former;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import repository.repos.sensor.SensorRepository;
import service.calculation.CalculationConfigHolder;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;
import util.DateHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.Objects;

import static util.StringHelper.FOR_LAST_ZERO;

public class TemplateExelTemperatureProtocolFormer implements ExelProtocolFormer {
    private static final String EXTRAORDINARY = "Позачерговий";

    private final HSSFWorkbook book;
    private final CalculationConfigHolder configHolder;
    private final RepositoryFactory repositoryFactory;

    private TemplateExelTemperatureProtocolFormer(@Nonnull HSSFWorkbook book,
                                                  @Nonnull CalculationConfigHolder configHolder,
                                                  @Nonnull RepositoryFactory repositoryFactory) {
        this.book = book;
        this.configHolder = configHolder;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public HSSFWorkbook format(Protocol protocol) {
        MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, protocol.getChannel());

        appendMainInfo(protocol);
        appendChannelInfo(protocol);
        appendSensorInfo(repositoryFactory, protocol, errorCalculater);

        return book;
    }

    void appendMainInfo(Protocol protocol) {
        boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();

        final String protocolNumber = protocol.getNumber();
        cell(12,2).setCellValue(protocolNumber);
        cell(12,11).setCellValue(protocolNumber);
        if (!suitable) {
            cell(18, 20).setCellValue(protocolNumber);
        }

        final String checkDate = protocol.getDate();
        cell(12,5).setCellValue(checkDate);
        cell(12,14).setCellValue(checkDate);
        if (!suitable) {
            cell(10, 24).setCellValue(checkDate);
        }

        final String externalTemperature = String.valueOf(protocol.getTemperature());
        cell(25,4).setCellValue(externalTemperature);

        final String humidity = String.valueOf(protocol.getHumidity());
        cell(26,4).setCellValue(humidity);

        final String atmospherePressure = String.valueOf(protocol.getPressure());
        cell(27,4).setCellValue(atmospherePressure);

        final String methodName = configHolder.getTemperatureCalculationMethodName();
        cell(32,15).setCellValue(methodName);

        String nextDate;
        if (suitable){
            nextDate = DateHelper.getNextDate(checkDate, protocol.getChannel().getFrequency());
            if (nextDate.isEmpty()) nextDate = EXTRAORDINARY;
        }else nextDate = EXTRAORDINARY;
        cell(38,14).setCellValue(nextDate);
    }

    void appendChannelInfo(Protocol protocol) {
        Channel channel = protocol.getChannel();
        boolean suitable = protocol.getRelativeError() <= channel.getAllowableErrorPercent();

        final String name = channel.getName();
        cell(10,0).setCellValue(name);
        cell(10,9).setCellValue(name);
        cell(34,9).setCellValue(name);
        if (!suitable) {
            cell(13, 18).setCellValue(name);
        }

        final String code = channel.getCode();
        cell(14,4).setCellValue(code);

        final String technologyNumber = channel.getTechnologyNumber();
        cell(15,4).setCellValue(technologyNumber);
        cell(14,13).setCellValue(technologyNumber);

        final String area = channel.getArea();
        cell(16,4).setCellValue(area);
        cell(15,13).setCellValue(area);
        cell(41,3).setCellValue(area);
        cell(43,12).setCellValue(area);
        if (!suitable) {
            cell(14, 22).setCellValue(area);
            cell(29, 21).setCellValue(area);
        }

        final String process = channel.getProcess();
        cell(16,6).setCellValue(process);
        cell(15,15).setCellValue(process);
        if (!suitable) {
            cell(14, 24).setCellValue(process);
        }

        final String rangeMin = StringHelper.roundingDouble(channel.getRangeMin(), protocol.getValuesDecimalPoint());
        cell(17,5).setCellValue(rangeMin);

        final String rangeMax = StringHelper.roundingDouble(channel.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(17,7).setCellValue(rangeMax);

        final String allowableErrorInPercent = StringHelper.roundingDouble(channel.getAllowableErrorPercent(), protocol.getPercentsDecimalPoint());
        cell(18,5).setCellValue(allowableErrorInPercent);
        cell(36,15).setCellValue(allowableErrorInPercent);

        final String allowableErrorInValue = StringHelper.roundingDouble(channel.getAllowableErrorValue(), protocol.getValuesDecimalPoint());
        cell(18,7).setCellValue(allowableErrorInValue);

        final String measurementValue = channel.getMeasurementValue();
        cell(17,8).setCellValue(measurementValue);
        cell(18,8).setCellValue(measurementValue);
        cell(21,8).setCellValue(measurementValue);
        cell(22,8).setCellValue(measurementValue);
        cell(32,2).setCellValue(measurementValue);
        cell(19,16).setCellValue(measurementValue);
        cell(24,15).setCellValue(measurementValue);
        cell(27,15).setCellValue(measurementValue);
        cell(28,15).setCellValue(measurementValue);
        cell(29,15).setCellValue(measurementValue);
        cell(30,15).setCellValue(measurementValue);

        final String frequency = StringHelper.roundingDouble(channel.getFrequency(), FOR_LAST_ZERO);
        cell(24,16).setCellValue(String.format("%sр.", frequency));
    }

    private void appendSensorInfo(RepositoryFactory repositoryFactory, Protocol protocol, MxParserErrorCalculater errorCalculater) {
        Channel channel = protocol.getChannel();
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        Sensor sensor = sensorRepository.get(channel.getCode());
        if (Objects.isNull(sensor)) return;

        String type = sensor.getType();
        cell(20,4).setCellValue(type);

        double errorSensor = errorCalculater.calculate(sensor);
        if (Double.isNaN(errorSensor)) return;
        double eP = errorSensor / (sensor.calculateRange() / 100);
        String errorPercent = StringHelper.roundingDouble(eP, protocol.getPercentsDecimalPoint());
        cell(21,5).setCellValue(errorPercent);

        String error = StringHelper.roundingDouble(errorSensor, protocol.getValuesDecimalPoint());
        cell(21,7).setCellValue(error);

        String rangeMin = StringHelper.roundingDouble(sensor.getRangeMin(), protocol.getValuesDecimalPoint());
        cell(22,5).setCellValue(rangeMin);

        String rangeMax = StringHelper.roundingDouble(sensor.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(22,7).setCellValue(rangeMax);
    }

    void appendCalibratorInfo(Protocol protocol, MxParserErrorCalculater errorCalculater) {
        Calibrator calibrator = protocol.getCalibrator();

        String type = calibrator.getType();
        cell(16,15).setCellValue(type);

        String number = calibrator.getNumber();
        cell(17,12).setCellValue(number);

        Calibrator.Certificate certificate = calibrator.getCertificate();
        cell(18,9).setCellValue(certificate.getType());
        cell(18,12).setCellValue(certificate.toString());

        double errorCalibrator = errorCalculater.calculate(calibrator);
        if (Double.isNaN(errorCalibrator)) return;
        double eP = errorCalibrator / (protocol.getChannel().calculateRange() / 100);
        String errorPercent = StringHelper.roundingDouble(eP, protocol.getPercentsDecimalPoint());
        cell(19,13).setCellValue(errorPercent);

        String error = StringHelper.roundingDouble(errorCalibrator, protocol.getValuesDecimalPoint());
        cell(19,15).setCellValue(error);
    }

    private void appendCalculationResult(Protocol protocol) {

    }

    private HSSFCell cell(int row, int column){
        HSSFSheet sheet = this.book.getSheetAt(0);
        HSSFRow Row = sheet.getRow(row);
        return Row.getCell(column);
    }
}
