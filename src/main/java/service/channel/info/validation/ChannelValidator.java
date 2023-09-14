package service.channel.info.validation;

import exception.ValidationException;
import model.dto.fields.ChannelField;
import model.dto.fields.SensorField;
import repository.repos.channel.ChannelRepository;
import service.channel.info.model.Channel;
import util.ValidationUtil;

import javax.annotation.Nullable;

public class ChannelValidator {
    public void validate(@Nullable model.dto.Channel oldChannel, Channel newChannel, ChannelRepository repository) {
        ValidationUtil.validateRequired(ChannelField.CODE, ChannelField.UI.CODE, newChannel.getCode());
        ValidationUtil.validateRequired(ChannelField.NAME, ChannelField.UI.NAME, newChannel.getName());
        ValidationUtil.validateRequired(ChannelField.MEASUREMENT_NAME, ChannelField.UI.MEASUREMENT_NAME, newChannel.getMeasurementName());
        ValidationUtil.validateRequired(ChannelField.MEASUREMENT_VALUE, ChannelField.UI.MEASUREMENT_VALUE, newChannel.getMeasurementValue());
        ValidationUtil.validateRequired(ChannelField.RANGE_MIN, ChannelField.UI.RANGE_MIN, newChannel.getRangeMin());
        ValidationUtil.validateRequired(ChannelField.RANGE_MAX, ChannelField.UI.RANGE_MAX, newChannel.getRangeMax());
        ValidationUtil.validateRequired(ChannelField.DATE, ChannelField.UI.DATE, newChannel.getDate());
        ValidationUtil.validateRequired(ChannelField.FREQUENCY, ChannelField.UI.FREQUENCY, newChannel.getFrequency());
        ValidationUtil.validateRequired(SensorField.TYPE, SensorField.UI.TYPE, newChannel.getSensorType());
        ValidationUtil.validateRequired(SensorField.ERROR_FORMULA, SensorField.UI.ERROR_FORMULA, newChannel.getSensorErrorFormula());
        ValidationUtil.validateRequired(SensorField.MEASUREMENT_VALUE, SensorField.UI.MEASUREMENT_VALUE, newChannel.getSensorMeasurementValue());
        ValidationUtil.validateRequired(SensorField.RANGE_MIN, SensorField.UI.RANGE_MIN, newChannel.getSensorRangeMin());
        ValidationUtil.validateRequired(SensorField.RANGE_MAX, SensorField.UI.RANGE_MAX, newChannel.getSensorRangeMax());
        ValidationUtil.validateRequired(ChannelField.ALLOWABLE_ERROR_VALUE, ChannelField.UI.ALLOWABLE_ERROR_VALUE, newChannel.getAllowableErrorValue());
        ValidationUtil.validateRequired(ChannelField.ALLOWABLE_ERROR_PERCENT, ChannelField.UI.ALLOWABLE_ERROR_PERCENT, newChannel.getAllowableErrorPercent());

        ValidationUtil.validateDate(ChannelField.DATE, ChannelField.UI.DATE, newChannel.getDate());

        ValidationUtil.validateDouble(ChannelField.FREQUENCY, ChannelField.UI.FREQUENCY, newChannel.getFrequency());
        ValidationUtil.validateDouble(ChannelField.RANGE_MIN, ChannelField.UI.RANGE_MIN, newChannel.getRangeMin());
        ValidationUtil.validateDouble(ChannelField.RANGE_MAX, ChannelField.UI.RANGE_MAX, newChannel.getRangeMax());
        ValidationUtil.validateDouble(ChannelField.ALLOWABLE_ERROR_VALUE, ChannelField.UI.ALLOWABLE_ERROR_VALUE, newChannel.getAllowableErrorValue());
        ValidationUtil.validateDouble(ChannelField.ALLOWABLE_ERROR_PERCENT, ChannelField.UI.ALLOWABLE_ERROR_PERCENT, newChannel.getAllowableErrorPercent());
        ValidationUtil.validateDouble(SensorField.RANGE_MIN, SensorField.UI.RANGE_MIN, newChannel.getSensorRangeMin());
        ValidationUtil.validateDouble(SensorField.RANGE_MAX, SensorField.UI.RANGE_MAX, newChannel.getSensorRangeMax());
        ValidationUtil.validateFormula(SensorField.ERROR_FORMULA, SensorField.UI.ERROR_FORMULA, newChannel.getSensorErrorFormula());

        if (oldChannel != null && !oldChannel.getCode().equals(newChannel.getCode())) {
            for (model.dto.Channel ch : repository.getAll()) {
                if (!ch.getCode().equals(oldChannel.getCode()) && ch.getCode().equals(newChannel.getCode())) {
                    throw ValidationException.B01(ChannelField.CODE, ChannelField.UI.CODE);
                }
            }
        }
    }
}
