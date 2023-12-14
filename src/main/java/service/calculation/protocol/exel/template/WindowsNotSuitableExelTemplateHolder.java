package service.calculation.protocol.exel.template;

import localization.Localization;
import model.dto.Calibrator;
import model.dto.Measurement;

public class WindowsNotSuitableExelTemplateHolder extends ExelTemplateHolder {
    @Override
    protected void initialization() {
        if (Localization.getCurrentLocalization() == Localization.UA) {
            templates.put(Measurement.TEMPERATURE, "forms/ukr/form_temperature_bad.xls");
            templates.put(Measurement.PRESSURE, "forms/ukr/form_pressure_bad.xls");
            templates.put(Measurement.CONSUMPTION, "forms/ukr/form_consumption_bad.xls");
            templates.put(String.format("%s_%s", Measurement.CONSUMPTION, Calibrator.ROSEMOUNT_8714DQ4), "forms/ukr/form_consumption_rosemount_bad.xls");
        }
    }
}
