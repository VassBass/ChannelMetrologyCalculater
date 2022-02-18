package ui.controlPointsValues.complexElements;

public class TemperaturePanel extends ControlPointsPanel {

    public TemperaturePanel(double rangeMin, double rangeMax){
        super(rangeMin, rangeMax);
        double[]percentValues = new double[]{0D,5D,50D,95D,100D};
        super.create(percentValues);
    }
}
