package service.person.info.ui.swing;

import localization.Labels;
import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoPatronymicPanel;

import java.awt.*;

public class SwingPersonInfoPatronymicPanel extends TitledTextField implements PersonInfoPatronymicPanel {
    private static final String PATRONYMIC = "patronymic";

    public SwingPersonInfoPatronymicPanel() {
        super(20, Labels.getLabels(SwingPersonInfoPatronymicPanel.class).get(PATRONYMIC), Color.BLACK);
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
