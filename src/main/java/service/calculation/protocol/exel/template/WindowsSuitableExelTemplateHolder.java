package service.calculation.protocol.exel.template;

import model.dto.Calibrator;
import model.dto.Measurement;

public class WindowsSuitableExelTemplateHolder extends ExelTemplateHolder {
    @Override
    protected void initialization() {
        templates.put(Measurement.TEMPERATURE, "forms/form_temperature_good.xls");
        templates.put(Measurement.PRESSURE, "forms/form_pressure_good.xls");
        templates.put(Measurement.CONSUMPTION, "forms/form_consumption_good.xls");
        templates.put(String.format("%s_%s", Measurement.CONSUMPTION, Calibrator.ROSEMOUNT_8714DQ4), "forms/form_consumption_rosemount_good.xls");
    }
}
