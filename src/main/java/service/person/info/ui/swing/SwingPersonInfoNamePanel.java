package service.person.info.ui.swing;

import localization.Labels;
import model.ui.TitledTextField;
import service.person.info.ui.PersonInfoNamePanel;

import javax.annotation.Nonnull;
import java.awt.*;

public class SwingPersonInfoNamePanel extends TitledTextField implements PersonInfoNamePanel {
    private static final String NAME = "name";

    public SwingPersonInfoNamePanel() {
        super(20, Labels.getLabels(SwingPersonInfoNamePanel.class).get(NAME), Color.BLACK);
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
