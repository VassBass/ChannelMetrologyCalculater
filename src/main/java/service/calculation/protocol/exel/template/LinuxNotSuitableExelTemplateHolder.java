package service.calculation.protocol.exel.template;

import model.dto.Calibrator;
import model.dto.Measurement;

public class LinuxNotSuitableExelTemplateHolder extends ExelTemplateHolder {
    @Override
    protected void initialization() {
        templates.put(Measurement.TEMPERATURE, "forms/linuxForm_temperature_bad.xls");
        templates.put(Measurement.PRESSURE, "forms/linuxForm_pressure_bad.xls");
        templates.put(Measurement.CONSUMPTION, "forms/linuxForm_consumption_bad.xls");
        templates.put(String.format("%s_%s", Measurement.CONSUMPTION, Calibrator.ROSEMOUNT_8714DQ4), "forms/linuxForm_consumption_rosemount_bad.xls");
    }
}
