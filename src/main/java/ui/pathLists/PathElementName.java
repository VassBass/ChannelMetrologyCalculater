package ui.pathLists;

import application.Application;
import converters.ConverterUI;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathElementName extends JDialog {
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";

    private final PathListsDialog parent;

    private JButton buttonSave, buttonCancel;
    private JTextField elementName;

    private final String elementType;
    private final String oldNameOfElement;

    public PathElementName(PathListsDialog parent, String elementType, String elementName){
        super(parent, elementType, true);
        this.parent = parent;
        this.elementType = elementType;
        this.oldNameOfElement = elementName;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.elementName = new JTextField(10);
        this.elementName.setHorizontalAlignment(SwingConstants.CENTER);
        if (this.oldNameOfElement != null){
            this.elementName.setText(this.oldNameOfElement);
        }
        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);
    }

    private void build() {
        this.setSize(230,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(PathElementName.this)) return;
            dispose();
            if (elementName.getText().length() > 0) {
                switch (elementType) {
                    case PathListsDialog.DEPARTMENT:
                        Application.context.departmentService.set(oldNameOfElement, elementName.getText());
                        break;
                    case PathListsDialog.AREA:
                        Application.context.areaService.set(oldNameOfElement, elementName.getText());
                        break;
                    case PathListsDialog.PROCESS:
                        Application.context.processService.set(oldNameOfElement, elementName.getText());
                        break;
                    case PathListsDialog.INSTALLATION:
                        Application.context.installationService.set(oldNameOfElement, elementName.getText());
                        break;
                }
                parent.update(elementType);
            }
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(elementName, new Cell(0,0,2,0.9));
            this.add(buttonCancel, new Cell(0,1,1,0.1));
            this.add(buttonSave, new Cell(1,1,1,0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, double height){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.weighty = height;
                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}