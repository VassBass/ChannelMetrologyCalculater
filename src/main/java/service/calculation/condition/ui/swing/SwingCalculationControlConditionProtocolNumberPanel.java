package service.calculation.condition.ui.swing;

import localization.label.Labels;
import model.ui.TitledTextField;
import service.calculation.condition.ui.CalculationControlConditionProtocolNumberPanel;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SwingCalculationControlConditionProtocolNumberPanel extends TitledTextField implements CalculationControlConditionProtocolNumberPanel {
    public SwingCalculationControlConditionProtocolNumberPanel() {
        super(4, Labels.getInstance().protocolNumber);
        this.setHorizontalAlignment(CENTER);
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingCalculationControlConditionProtocolNumberPanel.this.selectAll();
            }
        });
    }


    @Override
    public void setProtocolNumber(@Nonnull String number) {
        this.setText(number);
    }

    @Override
    public String getProtocolNumber() {
        String number = this.getText();

        if (number.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else this.setTitleColor(Color.BLACK);

        return number;
    }
}
