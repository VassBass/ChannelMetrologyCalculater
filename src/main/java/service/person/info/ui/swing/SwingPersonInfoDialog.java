package service.person.info.ui.swing;

import model.dto.Person;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.person.info.PersonInfoConfigHolder;
import service.person.info.ui.PersonInfoContext;
import service.person.list.ui.swing.SwingPersonListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SwingPersonInfoDialog extends JDialog implements UI {


    public SwingPersonInfoDialog(@Nonnull SwingPersonListDialog parentDialog,
                                 @Nonnull PersonInfoConfigHolder configHolder,
                                 @Nonnull PersonInfoContext context,
                                 @Nullable Person oldPerson) {
        super(parentDialog,
                Objects.isNull(oldPerson) ? "Новий співробітник" : String.format("ID співробітника %s", oldPerson.getId()),
                true);

        SwingPersonInfoSurnamePanel surnamePanel = context.getElement(SwingPersonInfoSurnamePanel.class);
        SwingPersonInfoNamePanel namePanel = context.getElement(SwingPersonInfoNamePanel.class);
        SwingPersonInfoPatronymicPanel patronymicPanel = context.getElement(SwingPersonInfoPatronymicPanel.class);
        SwingPersonInfoPositionPanel positionPanel = context.getElement(SwingPersonInfoPositionPanel.class);
        SwingPersonInfoButtonsPanel buttonsPanel = context.getElement(SwingPersonInfoButtonsPanel.class);

        if (Objects.nonNull(oldPerson)) {
            surnamePanel.setSurname(oldPerson.getSurname());
            namePanel.setPersonName(oldPerson.getName());
            patronymicPanel.setPatronymic(oldPerson.getPatronymic());
            positionPanel.setPosition(oldPerson.getPosition());
        }

        DefaultPanel panel = new DefaultPanel();
        panel.add(surnamePanel, new CellBuilder().y(0).build());
        panel.add(namePanel, new CellBuilder().y(1).build());
        panel.add(patronymicPanel, new CellBuilder().y(2).build());
        panel.add(positionPanel, new CellBuilder().y(3).build());
        panel.add(buttonsPanel, new CellBuilder().y(4).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parentDialog, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }

    @Override
    public Object getSource() {
        return this;
    }
}
