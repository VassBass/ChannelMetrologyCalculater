package service.calculation.protocol.exel.former;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import repository.repos.calculation_method.CalculationMethodRepository;
import repository.repos.sensor.SensorRepository;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;
import util.DateHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static util.StringHelper.FOR_LAST_ZERO;

public class TemplateExelConsumptionProtocolFormer extends TemplateExelTemperatureProtocolFormer {
    private static final String NOT_SUITABLE_TEXT = "НЕ ПРИДАТНИМ ДО ЕКСПЛУАТАЦІЇ для комерційного обліку";

    public TemplateExelConsumptionProtocolFormer(@Nonnull HSSFWorkbook book,
                                                 @Nonnull RepositoryFactory repositoryFactory) {
        super(book, repositoryFactory);
    }

    @Override
    protected void appendMainInfo(Protocol protocol) {
        final CalculationMethodRepository methodRepository = repositoryFactory.getImplementation(CalculationMethodRepository.class);

        final boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();
        final boolean isRosemount8714DQ4Calibrator = protocol.getCalibrator().getName().equals(Calibrator.ROSEMOUNT_8714DQ4);

        final String protocolNumber = protocol.getNumber();
        cell(13,2).setCellValue(protocolNumber);
        cell(13,11).setCellValue(protocolNumber);
        if (!suitable) {
            cell(19, 20).setCellValue(protocolNumber);
        }

        final String checkDate = protocol.getDate();
        cell(13,5).setCellValue(checkDate);
        cell(13,14).setCellValue(checkDate);
        if (!suitable) {
            cell(10, 24).setCellValue(checkDate);
        }

        final String externalTemperature = String.valueOf(protocol.getTemperature());
        cell(23,4).setCellValue(externalTemperature);

        final String humidity = String.valueOf(protocol.getHumidity());
        cell(24,4).setCellValue(humidity);

        final String atmospherePressure = String.valueOf(protocol.getPressure());
        cell(25,4).setCellValue(atmospherePressure);

        final String methodName = methodRepository.getMethodNameByMeasurementName(protocol.getChannel().getMeasurementName());
        if (Objects.nonNull(methodName)) {
            if (isRosemount8714DQ4Calibrator) {
                cell(32, 15).setCellValue(methodName);
            } else {
                cell(33, 15).setCellValue(methodName);
            }
        }

        String nextDate;
        if (suitable){
            nextDate = DateHelper.getNextDate(checkDate, protocol.getChannel().getFrequency());
            if (nextDate.isEmpty()) nextDate = EXTRAORDINARY;
        }else nextDate = EXTRAORDINARY;
        if (isRosemount8714DQ4Calibrator) {
            cell(38, 14).setCellValue(nextDate);
        } else {
            cell(39, 14).setCellValue(nextDate);
        }

        final String numberOfReference = protocol.getReferenceNumber();
        cell(10,21).setCellValue(numberOfReference);
    }

    @Override
    protected void appendChannelInfo(Protocol protocol) {
        final Channel channel = protocol.getChannel();
        final boolean suitable = protocol.getRelativeError() <= channel.getAllowableErrorPercent();
        final boolean isRosemount8714DQ4Calibrator = protocol.getCalibrator().getName().equals(Calibrator.ROSEMOUNT_8714DQ4);

        final String name = channel.getName();
        cell(10,0).setCellValue(name);
        cell(10,9).setCellValue(name);
        if (!suitable) {
            cell(13, 18).setCellValue(name);
        }

        final String code = channel.getCode();
        cell(15,4).setCellValue(code);

        final String technologyNumber = channel.getTechnologyNumber();
        cell(16,4).setCellValue(technologyNumber);
        cell(15,13).setCellValue(technologyNumber);

        final String area = channel.getArea();
        cell(17,4).setCellValue(area);
        cell(16,13).setCellValue(area);
        cell(41,3).setCellValue(area);
        cell(43,12).setCellValue(area);
        if (!suitable) {
            cell(16, 22).setCellValue(area);
            if (!isRosemount8714DQ4Calibrator) cell(29, 21).setCellValue(area);
        }

        final String process = channel.getProcess();
        cell(17,6).setCellValue(process);
        cell(16,15).setCellValue(process);
        if (!suitable) {
            cell(16, 24).setCellValue(process);
        }

        final String rangeMin = StringHelper.roundingDouble(channel.getRangeMin(), protocol.getValuesDecimalPoint());
        cell(19,5).setCellValue(rangeMin.replaceAll("\\.", ","));

        final String rangeMax = StringHelper.roundingDouble(channel.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(19,7).setCellValue(rangeMax.replaceAll("\\.", ","));

        final String allowableErrorInPercent = StringHelper.roundingDouble(channel.getAllowableErrorPercent(), protocol.getPercentsDecimalPoint())
                .replaceAll("\\.", ",");
        cell(20,5).setCellValue(allowableErrorInPercent);
        if (isRosemount8714DQ4Calibrator) {
            cell(37, 15).setCellValue(allowableErrorInPercent);
        } else {
            cell(38, 15).setCellValue(allowableErrorInPercent);
        }

        final String allowableErrorInValue = StringHelper.roundingDouble(channel.getAllowableErrorValue(), protocol.getValuesDecimalPoint());
        cell(20,7).setCellValue(allowableErrorInValue.replaceAll("\\.", ","));

        final String measurementValue = channel.getMeasurementValue();
        if (isRosemount8714DQ4Calibrator) {
            cell(30,2).setCellValue(measurementValue);
        } else {
            cell(29,2).setCellValue(measurementValue);
            cell(32,15).setCellValue(measurementValue);
        }
        cell(19,8).setCellValue(measurementValue);
        cell(20,8).setCellValue(measurementValue);
        cell(19,16).setCellValue(measurementValue);
        cell(24,15).setCellValue(measurementValue);
        cell(27,15).setCellValue(measurementValue);
        cell(28,15).setCellValue(measurementValue);
        cell(29,15).setCellValue(measurementValue);
        cell(30,15).setCellValue(measurementValue);
        cell(31,15).setCellValue(measurementValue);

        final String frequency = StringHelper.roundingDouble(channel.getFrequency(), FOR_LAST_ZERO).replaceAll("\\.", ",");
        cell(24,16).setCellValue(String.format("%sр.", frequency));
    }

    @Override
    protected void appendSensorInfo(RepositoryFactory repositoryFactory, Protocol protocol, MxParserErrorCalculater errorCalculater) {
        final Channel channel = protocol.getChannel();
        final SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        final Sensor sensor = sensorRepository.get(channel.getCode());
        if (Objects.isNull(sensor)) return;
        final boolean notSuitable = channel.getAllowableErrorPercent() < protocol.getRelativeError();
        final boolean isRosemount8714DQ4Calibrator = protocol.getCalibrator().getName().equals(Calibrator.ROSEMOUNT_8714DQ4);

        final String type = sensor.getType();
        cell(11,4).setCellValue(type);
        cell(11,13).setCellValue(type);
        if (isRosemount8714DQ4Calibrator) {
            cell(33, 11).setCellValue(type);
        } else {
            cell(34, 11).setCellValue(type);
        }
        if (notSuitable){
            cell(14,20).setCellValue(type);
        }

        final String number = sensor.getSerialNumber();
        cell(18,4).setCellValue(number);
        if (isRosemount8714DQ4Calibrator) {
            cell(34, 11).setCellValue(number);
        } else {
            cell(35, 11).setCellValue(number);
        }
        if (notSuitable){
            cell(15,20).setCellValue(number);
        }
    }

    @Override
    protected void appendCalibratorInfo(Protocol protocol, MxParserErrorCalculater errorCalculater) {
        Calibrator calibrator = protocol.getCalibrator();

        String type = calibrator.getType();
        cell(17,15).setCellValue(type);

        String number = calibrator.getNumber();
        cell(18,12).setCellValue(number);

        Calibrator.Certificate certificate = calibrator.getCertificate();
        cell(19,9).setCellValue(certificate.getType());
        cell(19,12).setCellValue(certificate.toString());

        double errorCalibrator = errorCalculater.calculate(calibrator);
        if (Double.isNaN(errorCalibrator)) return;
        double eP = errorCalibrator / (protocol.getChannel().calculateRange() / 100);
        String errorPercent = StringHelper.roundingDouble(eP, protocol.getPercentsDecimalPoint()).replaceAll("\\.", ",");
        cell(20,13).setCellValue(errorPercent);

        String errorValue = StringHelper.roundingDouble(errorCalibrator, protocol.getValuesDecimalPoint()).replaceAll("\\.", ",");
        cell(20,15).setCellValue(errorValue);
    }

    @Override
    protected void appendCalculationResult(Protocol protocol) {
        final int valueDecimalPoint = protocol.getValuesDecimalPoint();
        final int percentDecimalPoint = protocol.getPercentsDecimalPoint();
        final boolean notSuitable = protocol.getChannel().getAllowableErrorPercent() < protocol.getRelativeError();
        final boolean isRosemount8714DQ4Calibrator = protocol.getCalibrator().getName().equals(Calibrator.ROSEMOUNT_8714DQ4);

        if (!isRosemount8714DQ4Calibrator) {
            final TreeMap<Double, Double> input = protocol.getInput();
            int row = 30;
            for (Map.Entry<Double, Double> entry : input.entrySet()) {
                cell(row, 1).setCellValue(StringHelper.roundingDouble(entry.getKey(), percentDecimalPoint).replaceAll("\\.", ","));
                row += 2;
            }
            row = 28;
            for (Map.Entry<Double, Double> entry : input.entrySet()) {
                String pe = StringHelper.roundingDouble(Double.parseDouble(StringHelper.roundingDouble(entry.getKey(), percentDecimalPoint)), FOR_LAST_ZERO);
                cell(row++, 11).setCellValue(String.format("%s%% ΔS =", pe.replaceAll("\\.", ",")));
            }
        }

        final TreeMap<Double, double[]> inputOutput = protocol.getOutput();
        int row = 30;
        if (isRosemount8714DQ4Calibrator) row = 31;
        int column = 3;
        boolean next = false;
        for (Map.Entry<Double, double[]> entry : inputOutput.entrySet()) {
            cell(row, 2).setCellValue(StringHelper.roundingDouble(entry.getKey(), valueDecimalPoint).replaceAll("\\.", ","));
            for (double d : entry.getValue()) {
                cell(row, column).setCellValue(StringHelper.roundingDouble(d, valueDecimalPoint).replaceAll("\\.", ","));
                if (next) {
                    next = false;
                    column++;
                    row--;
                } else {
                    next = true;
                    row++;
                }
            }
            if (isRosemount8714DQ4Calibrator) {
                row = row < 33 ? 33 : row < 35 ? 35 : 37;
            } else {
                row = row < 32 ? 32 : row < 34 ? 34 : row < 36 ? 36 : 38;
            }
            column = 3;
        }

        final String u = StringHelper.roundingDouble(protocol.getExtendedIndeterminacy(), valueDecimalPoint).replaceAll("\\.", ",");
        cell(24, 14).setCellValue(u);

        final String relativeError = StringHelper.roundingDouble(protocol.getRelativeError(), percentDecimalPoint).replaceAll("\\.", ",");
        cell(26, 14).setCellValue(relativeError);
        if (isRosemount8714DQ4Calibrator) {
            cell(37, 13).setCellValue(relativeError);
        } else {
            cell(38, 13).setCellValue(relativeError);
        }

        final String absoluteError = StringHelper.roundingDouble(protocol.getAbsoluteError(), valueDecimalPoint).replaceAll("\\.", ",");
        cell(27, 14).setCellValue(absoluteError);

        final TreeMap<Double, Double> systematicErrors = protocol.getSystematicErrors();
        row = 28;
        for (Map.Entry<Double, Double> entry : systematicErrors.entrySet()) {
            cell(row++, 13).setCellValue(StringHelper.roundingDouble(entry.getValue(), valueDecimalPoint).replaceAll("\\.", ","));
        }

        if (notSuitable){
            if (isRosemount8714DQ4Calibrator) {
                cell(35, 11).setCellValue(NOT_SUITABLE_TEXT);
            } else {
                cell(36, 11).setCellValue(NOT_SUITABLE_TEXT);
            }
        }

        String conclusion = protocol.getConclusion();
        if (isRosemount8714DQ4Calibrator) {
            cell(36, 9).setCellValue(conclusion);
        } else {
            cell(37, 9).setCellValue(conclusion);
        }
    }
}
