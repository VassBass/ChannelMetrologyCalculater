package service.person.info.ui.swing;

import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoNamePanel;

import javax.annotation.Nonnull;
import java.awt.*;

public class SwingPersonInfoNamePanel extends TitledTextField implements PersonInfoNamePanel {
    private static final String TITLE_TEXT = "Ім'я";

    public SwingPersonInfoNamePanel() {
        super(20, TITLE_TEXT, Color.BLACK);
    }

    @Override
    public String getPersonName() {
        String name = this.getText();
        if (name.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return name;
    }

    @Override
    public void setPersonName(@Nonnull String name) {
        this.setText(name);
    }
}
