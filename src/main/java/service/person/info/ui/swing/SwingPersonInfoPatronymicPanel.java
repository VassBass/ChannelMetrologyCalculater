package service.person.info.ui.swing;

import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoPatronymicPanel;

import java.awt.*;

public class SwingPersonInfoPatronymicPanel extends TitledTextField implements PersonInfoPatronymicPanel {
    private static final String TITLE_TEXT = "По батькові";

    public SwingPersonInfoPatronymicPanel() {
        super(20, TITLE_TEXT, Color.BLACK);
    }


    @Override
    public String getPatronymic() {
        String patronymic = this.getText();
        if (patronymic.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return patronymic;
    }

    @Override
    public void setPatronymic(String patronymic) {
        this.setText(patronymic);
    }
}
