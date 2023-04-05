package service.calculation.protocol.exel.template;

import model.dto.Calibrator;
import model.dto.Measurement;

public class WindowsNotSuitableExelTemplateHolder extends ExelTemplateHolder {
    @Override
    protected void initialization() {
        templates.put(Measurement.TEMPERATURE, "forms/form_temperature_bad.xls");
        templates.put(Measurement.PRESSURE, "forms/form_pressure_bad.xls");
        templates.put(Measurement.CONSUMPTION, "forms/form_consumption_bad.xls");
        templates.put(String.format("%s_%s", Measurement.CONSUMPTION, Calibrator.ROSEMOUNT_8714DQ4), "forms/form_consumption_rosemount_bad.xls");
    }
}
