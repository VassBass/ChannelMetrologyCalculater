package ui.controlPointsValues.complexElements;

import converters.VariableConverter;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TopPanel extends JPanel {
    private static final String RANGE = "Діапазон вимірювального каналу:";
    private static final String TREE_DOTS = " ... ";
    private static final String DEFAULT_RANGE_MIN = "0.00";
    private static final String DEFAULT_RANGE_MAX = "100.00";
    private static final String CLEAR = "Очистити";

    private final JLabel rangeLabel = new JLabel(RANGE);
    private final JLabel treeDots = new JLabel(TREE_DOTS);
    private final JTextField rangeMin = new JTextField(DEFAULT_RANGE_MIN, 5);
    private final JTextField rangeMax = new JTextField(DEFAULT_RANGE_MAX, 5);
    private final JButton btnClear = new DefaultButton(CLEAR);

    public TopPanel(final ControlPointsPanel controlPointsPanel){
        super(new GridBagLayout());

        this.btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlPointsPanel.clear();
            }
        });
        this.rangeMin.addFocusListener(rangeFocus);
        this.rangeMax.addFocusListener(rangeFocus);

        JPanel rangePanel = new JPanel();
        rangePanel.add(rangeLabel);
        rangePanel.add(rangeMin);
        rangePanel.add(treeDots);
        rangePanel.add(rangeMax);
        this.add(rangePanel, new Cell(0, 0.8));
        this.add(btnClear, new Cell(1, 0.2));
    }

    public double getRangeMin(){return Double.parseDouble(this.rangeMin.getText());}
    public double getRangeMax(){return Double.parseDouble(this.rangeMax.getText());}

    public final FocusListener rangeFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String text = source.getText();
            source.setText(VariableConverter.doubleString(text));
            setTrueRange();
        }
    };

    private void setTrueRange(){
        double rMin = Double.parseDouble(this.rangeMin.getText());
        double rMax = Double.parseDouble(this.rangeMax.getText());
        if (rMin > rMax){
            this.rangeMax.setText(String.valueOf(rMin));
            this.rangeMin.setText(String.valueOf(rMax));
        }
    }

    private static class Cell extends GridBagConstraints {
        protected Cell(int x, double weight){
            super();

            this.fill = BOTH;
            this.gridx = x;
            this.gridy = 0;
            this.weightx = weight;
        }
    }
}
