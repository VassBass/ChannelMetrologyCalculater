package service.person.info.ui.swing;

import localization.Labels;
import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoSurnamePanel;

import java.awt.*;

public class SwingPersonInfoSurnamePanel extends TitledTextField implements PersonInfoSurnamePanel {
    private static final String LAST_NAME = "lastName";

    public SwingPersonInfoSurnamePanel() {
        super(20, Labels.getLabels(SwingPersonInfoSurnamePanel.class).get(LAST_NAME), Color.BLACK);
    }

    @Override
    public String getSurname() {
        String surname = this.getText();
        if (surname.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.setText(surname);
    }
}
