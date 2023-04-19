package service.calculation.protocol.exel.former;

import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Sensor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import repository.repos.calculation_method.CalculationMethodRepository;
import repository.repos.sensor.SensorRepository;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;
import util.DateHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static util.StringHelper.FOR_LAST_ZERO;

public class TemplateExelTemperatureProtocolFormer implements ExelProtocolFormer {
    protected static final String EXTRAORDINARY = "Позачерговий";

    private final HSSFWorkbook book;
    protected final RepositoryFactory repositoryFactory;

    public TemplateExelTemperatureProtocolFormer(@Nonnull HSSFWorkbook book,
                                                  @Nonnull RepositoryFactory repositoryFactory) {
        this.book = book;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public HSSFWorkbook format(Protocol protocol) {
        final MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, protocol.getChannel());

        appendMainInfo(protocol);
        appendChannelInfo(protocol);
        appendSensorInfo(repositoryFactory, protocol, errorCalculater);
        appendCalibratorInfo(protocol, errorCalculater);
        appendCalculationResult(protocol);
        appendPersons(protocol);

        return book;
    }

    protected void appendMainInfo(Protocol protocol) {
        final CalculationMethodRepository calculationMethodRepository = repositoryFactory.getImplementation(CalculationMethodRepository.class);

        final boolean suitable = protocol.getChannel().getAllowableErrorPercent() >= protocol.getRelativeError();

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

        final String methodName = calculationMethodRepository.getMethodNameByMeasurementName(protocol.getChannel().getMeasurementName());
        if (Objects.nonNull(methodName)) cell(32,15).setCellValue(methodName);

        String nextDate;
        if (suitable){
            nextDate = DateHelper.getNextDate(checkDate, protocol.getChannel().getFrequency());
            if (nextDate.isEmpty()) nextDate = EXTRAORDINARY;
        }else nextDate = EXTRAORDINARY;
        cell(38,14).setCellValue(nextDate);

        final String numberOfReference = protocol.getReferenceNumber();
        cell(10,21).setCellValue(numberOfReference);
    }

    protected void appendChannelInfo(Protocol protocol) {
        final Channel channel = protocol.getChannel();
        final boolean suitable = protocol.getRelativeError() <= channel.getAllowableErrorPercent();

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
        cell(17,5).setCellValue(rangeMin.replaceAll("\\.", ","));

        final String rangeMax = StringHelper.roundingDouble(channel.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(17,7).setCellValue(rangeMax.replaceAll("\\.", ","));

        final String allowableErrorInPercent = StringHelper.roundingDouble(channel.getAllowableErrorPercent(), protocol.getPercentsDecimalPoint())
                .replaceAll("\\.", ",");
        cell(18,5).setCellValue(allowableErrorInPercent);
        cell(36,15).setCellValue(allowableErrorInPercent);

        final String allowableErrorInValue = StringHelper.roundingDouble(channel.getAllowableErrorValue(), protocol.getValuesDecimalPoint());
        cell(18,7).setCellValue(allowableErrorInValue.replaceAll("\\.", ","));

        final String measurementValue = channel.getMeasurementValue();
        cell(17,8).setCellValue(measurementValue);
        cell(18,8).setCellValue(measurementValue);
        cell(21,8).setCellValue(measurementValue);
        cell(32,2).setCellValue(measurementValue);
        cell(19,16).setCellValue(measurementValue);
        cell(24,15).setCellValue(measurementValue);
        cell(27,15).setCellValue(measurementValue);
        cell(28,15).setCellValue(measurementValue);
        cell(29,15).setCellValue(measurementValue);
        cell(30,15).setCellValue(measurementValue);

        final String frequency = StringHelper.roundingDouble(channel.getFrequency(), FOR_LAST_ZERO).replaceAll("\\.", ",");
        cell(24,16).setCellValue(String.format("%sр.", frequency));
    }

    protected void appendSensorInfo(RepositoryFactory repositoryFactory, Protocol protocol, MxParserErrorCalculater errorCalculater) {
        final Channel channel = protocol.getChannel();
        final SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        final Sensor sensor = sensorRepository.get(channel.getCode());
        if (Objects.isNull(sensor)) return;

        final String type = sensor.getType();
        cell(20,4).setCellValue(type);

        final double errorSensor = errorCalculater.calculate(sensor);
        if (Double.isNaN(errorSensor)) return;
        double eP = errorSensor / (channel.calculateRange() / 100);
        final String errorPercent = StringHelper.roundingDouble(eP, protocol.getPercentsDecimalPoint());
        cell(21,5).setCellValue(errorPercent.replaceAll("\\.", ","));

        final String errorValue = StringHelper.roundingDouble(errorSensor, protocol.getValuesDecimalPoint());
        cell(21,7).setCellValue(errorValue.replaceAll("\\.", ","));

        final String rangeMin = StringHelper.roundingDouble(sensor.getRangeMin(), protocol.getValuesDecimalPoint());
        cell(22,5).setCellValue(rangeMin.replaceAll("\\.", ","));

        final String rangeMax = StringHelper.roundingDouble(sensor.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(22,7).setCellValue(rangeMax.replaceAll("\\.", ","));

        final String measurementValue = sensor.getMeasurementValue();
        cell(22,8).setCellValue(measurementValue);
    }

    protected void appendCalibratorInfo(Protocol protocol, MxParserErrorCalculater errorCalculater) {
        final Calibrator calibrator = protocol.getCalibrator();

        final String type = calibrator.getType();
        cell(16,15).setCellValue(type);

        final String number = calibrator.getNumber();
        cell(17,12).setCellValue(number);

        final Calibrator.Certificate certificate = calibrator.getCertificate();
        cell(18,9).setCellValue(certificate.getType());
        cell(18,12).setCellValue(certificate.toString());

        final double errorCalibrator = errorCalculater.calculate(calibrator);
        if (Double.isNaN(errorCalibrator)) return;
        double eP = errorCalibrator / (protocol.getChannel().calculateRange() / 100);
        final String errorPercent = StringHelper.roundingDouble(eP, protocol.getPercentsDecimalPoint());
        cell(19,13).setCellValue(errorPercent.replaceAll("\\.", ","));

        final String errorValue = StringHelper.roundingDouble(errorCalibrator, protocol.getValuesDecimalPoint());
        cell(19,15).setCellValue(errorValue.replaceAll("\\.", ","));
    }

    protected void appendCalculationResult(Protocol protocol) {
        final int valueDecimalPoint = protocol.getValuesDecimalPoint();
        final int percentDecimalPoint = protocol.getPercentsDecimalPoint();

        final TreeMap<Double, Double> input = protocol.getInput();
        int row = 33;
        for (Map.Entry<Double, Double> entry : input.entrySet()) {
            cell(row, 1).setCellValue(StringHelper.roundingDouble(entry.getKey(), percentDecimalPoint).replaceAll("\\.", ","));
            row += 2;
        }
        row = 28;
        for (Map.Entry<Double, Double> entry : input.entrySet()) {
            String pe = StringHelper.roundingDouble(Double.parseDouble(StringHelper.roundingDouble(entry.getKey(), percentDecimalPoint)), FOR_LAST_ZERO);
            cell(row++, 11).setCellValue(String.format("%s%% ΔS =", pe.replaceAll("\\.", ",")));
        }

        final TreeMap<Double, double[]> inputOutput = protocol.getOutput();
        row = 33;
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
            row = row < 35 ? 35 : 37;
            column = 3;
        }

        final String u = StringHelper.roundingDouble(protocol.getExtendedIndeterminacy(), valueDecimalPoint).replaceAll("\\.", ",");
        cell(24, 14).setCellValue(u);

        final String relativeError = StringHelper.roundingDouble(protocol.getRelativeError(), percentDecimalPoint).replaceAll("\\.", ",");
        cell(26, 14).setCellValue(relativeError);
        cell(36,13).setCellValue(relativeError);

        final String absoluteError = StringHelper.roundingDouble(protocol.getAbsoluteError(), valueDecimalPoint).replaceAll("\\.", ",");
        cell(27, 14).setCellValue(absoluteError);

        final TreeMap<Double, Double> systematicErrors = protocol.getSystematicErrors();
        row = 28;
        for (Map.Entry<Double, Double> entry : systematicErrors.entrySet()) {
            cell(row++, 13).setCellValue(StringHelper.roundingDouble(entry.getValue(), valueDecimalPoint).replaceAll("\\.", ","));
        }

        final String conclusion = protocol.getConclusion();
        cell(37, 9).setCellValue(conclusion);
    }

    protected void appendPersons(Protocol protocol) {
        final boolean notSuitable = protocol.getChannel().getAllowableErrorPercent() < protocol.getRelativeError();

        String headOfCheckedChannelDepartment = protocol.getHeadOfCheckedChannelDepartment();
        if (headOfCheckedChannelDepartment.isEmpty()) headOfCheckedChannelDepartment = "________________";
        cell(41,6).setCellValue(headOfCheckedChannelDepartment);
        cell(43,15).setCellValue(headOfCheckedChannelDepartment);
        if (notSuitable){
            cell(29,24).setCellValue(headOfCheckedChannelDepartment);
        }

        String headOfMetrologyDepartment = protocol.getHeadOfMetrologyDepartment();
        if (headOfMetrologyDepartment.isEmpty()) headOfMetrologyDepartment = "________________";
        cell(43,6).setCellValue(headOfMetrologyDepartment);
        cell(45,15).setCellValue(headOfMetrologyDepartment);
        if (notSuitable){
            cell(32,24).setCellValue(headOfMetrologyDepartment);
            cell(47,24).setCellValue(headOfMetrologyDepartment);
        }

        final List<Map.Entry<String, String>> makers = protocol.getMakers();
        int row = 45;
        for (int index = 0; index < 2; index++) {
            if (makers.isEmpty() || index >= makers.size()) {
                cell(row, 0).setCellValue(EMPTY);
                cell(row, 4).setCellValue(EMPTY);
                cell(row, 6).setCellValue(EMPTY);
            } else {
                cell(row, 0).setCellValue(makers.get(index).getValue());
                cell(row, 6).setCellValue(makers.get(index).getKey());
            }
            row += 2;
        }

        final Map.Entry<String, String> former = protocol.getFormer();
        String formerName = former.getKey().isEmpty() ? "________________" : former.getKey();
        String formerPosition = former.getValue().isEmpty() ? "________________" : former.getValue();
        cell(47, 9).setCellValue(formerPosition);
        cell(47, 15).setCellValue(formerName);
        if (notSuitable) {
            cell(35, 18).setCellValue(formerPosition);
            cell(35, 24).setCellValue(formerName);
        }

        if (notSuitable){
            String headOfASUTPDepartment = protocol.getHeadOfASPCDepartment();
            if (headOfASUTPDepartment.isEmpty()) headOfASUTPDepartment = "________________";
            cell(26,24).setCellValue(headOfASUTPDepartment);
        }
    }

    protected HSSFCell cell(int row, int column){
        HSSFSheet sheet = this.book.getSheetAt(0);
        HSSFRow Row = sheet.getRow(row);
        return Row.getCell(column);
    }
}
