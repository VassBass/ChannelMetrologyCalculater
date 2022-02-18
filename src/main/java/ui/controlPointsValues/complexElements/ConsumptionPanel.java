package ui.controlPointsValues.complexElements;

public class ConsumptionPanel extends ControlPointsPanel {
    public ConsumptionPanel(double rangeMin, double rangeMax){
        super(rangeMin, rangeMax);
        double[]percentValues = new double[]{0D,25D,50D,75D,100D};
        super.create(percentValues);
    }
}
