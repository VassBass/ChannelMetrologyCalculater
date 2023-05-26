package service.calculation.protocol.exel.former;

import model.dto.Channel;
import model.dto.Sensor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import repository.RepositoryFactory;
import repository.repos.sensor.SensorRepository;
import service.calculation.protocol.Protocol;
import service.error_calculater.MxParserErrorCalculater;
import util.StringHelper;

import javax.annotation.Nonnull;

import java.util.Objects;

import static util.StringHelper.FOR_LAST_ZERO;

public class TemplateExelPressureProtocolFormer extends TemplateExelTemperatureProtocolFormer {

    public TemplateExelPressureProtocolFormer(@Nonnull HSSFWorkbook book,
                                               @Nonnull RepositoryFactory repositoryFactory) {
        super(book, repositoryFactory);
    }

    @Override
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
        if (!suitable) {
            cell(14, 22).setCellValue(area);
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
        cell(21,8).setCellValue(measurementValue);
        cell(21,7).setCellValue(measurementValue);
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

    @Override
    protected void appendSensorInfo(RepositoryFactory repositoryFactory, Protocol protocol, MxParserErrorCalculater errorCalculater) {
        final Channel channel = protocol.getChannel();
        final SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        final Sensor sensor = sensorRepository.get(channel.getCode());
        if (Objects.isNull(sensor)) return;

        final String type = sensor.getType();
        cell(20,4).setCellValue(type);

        final double errorSensor = errorCalculater.calculate(sensor);
        if (Double.isNaN(errorSensor)) return;
        double range = sensor.calculateRange();
        final double errorSensorPercent = (errorSensor / range) * 100;
        final String errorPercent = StringHelper.roundingDouble(errorSensorPercent, protocol.getPercentsDecimalPoint());
        final String errorValue = StringHelper.roundingDouble(errorSensor, protocol.getValuesDecimalPoint());
        cell(21,5).setCellValue(errorPercent.replaceAll("\\.", ","));
        cell(21,7).setCellValue(errorValue.replaceAll("\\.", ","));

        final String rangeMin = StringHelper.roundingDouble(sensor.getRangeMin(), protocol.getValuesDecimalPoint());
        cell(22,5).setCellValue(rangeMin.replaceAll("\\.", ","));

        final String rangeMax = StringHelper.roundingDouble(sensor.getRangeMax(), protocol.getValuesDecimalPoint());
        cell(22,7).setCellValue(rangeMax.replaceAll("\\.", ","));

        final String measurementValue = sensor.getMeasurementValue();
        cell(22,8).setCellValue(measurementValue);
    }
}
