package service.person.info.ui.swing;

import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoSurnamePanel;

import java.awt.*;

public class SwingPersonInfoSurnamePanel extends TitledTextField implements PersonInfoSurnamePanel {
    private static final String TITLE_TEXT = "Прізвище";

    public SwingPersonInfoSurnamePanel() {
        super(20, TITLE_TEXT, Color.BLACK);
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
