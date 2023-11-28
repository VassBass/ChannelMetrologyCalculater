package localization.label;

public class UkraineLabels extends Labels {

    protected UkraineLabels() {
        super();
    }

    @Override
    protected void init() {
        //Calibrator
        calibratorsList = "Список калібраторів";

        //Control points
        controlPointsOfMeasurements = "Контрольні точки вимірюваннь";

        //Measurement
        measurementsList = "Список вимірювальних величин";

        //Method
        calculationMethods = "Методи розрахунку";

        //Person
        personsList = "Список персоналу";

        //Sensor
        sensorsErrorList = "Список похибок ПВП";
        sensorTypesList = "Список типів ПВП";
    }
}
