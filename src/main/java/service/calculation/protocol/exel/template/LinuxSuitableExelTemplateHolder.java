package service.calculation.protocol.exel.template;

import localization.Localization;
import model.dto.Calibrator;
import model.dto.Measurement;

public class LinuxSuitableExelTemplateHolder extends ExelTemplateHolder {
    @Override
    protected void initialization() {
        if (Localization.getCurrentLocalization() == Localization.UA) {
            templates.put(Measurement.TEMPERATURE, "forms/ukr/linuxForm_temperature_good.xls");
            templates.put(Measurement.PRESSURE, "forms/ukr/linuxForm_pressure_good.xls");
            templates.put(Measurement.CONSUMPTION, "forms/ukr/linuxForm_consumption_good.xls");
            templates.put(String.format("%s_%s", Measurement.CONSUMPTION, Calibrator.ROSEMOUNT_8714DQ4), "forms/ukr/linuxForm_consumption_rosemount_good.xls");
        }
    }
}
