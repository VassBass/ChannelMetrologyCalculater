package ui.controlPointsValues.complexElements;

public class PressurePanel extends ControlPointsPanel {
    public PressurePanel(double rangeMin, double rangeMax){
        super(rangeMin, rangeMax);
        double[]percentValues = new double[]{0D,5D,50D,95D,100D};
        super.create(percentValues);
    }
}
