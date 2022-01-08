package ui.pathLists;

import backgroundTasks.controllers.PutPathElementInList;
import constants.Strings;
import converters.ConverterUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathElementName extends JDialog implements UI_Container {
    private final PathListsDialog parent;

    private final Color buttonsColor = new Color(51,51,51);

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

    @Override
    public void createElements() {
        this.elementName = new JTextField(10);
        this.elementName.setHorizontalAlignment(SwingConstants.CENTER);
        if (this.oldNameOfElement != null){
            this.elementName.setText(this.oldNameOfElement);
        }
        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonSave = new JButton(Strings.SAVE);
        this.buttonSave.setBackground(buttonsColor);
        this.buttonSave.setForeground(Color.white);
        this.buttonSave.setFocusPainted(false);
        this.buttonSave.setContentAreaFilled(false);
        this.buttonSave.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonSave.addChangeListener(pushButton);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonSave.addActionListener(clickSave);
    }

    @Override
    public void build() {
        this.setSize(230,100);
        this.setLocation(ConverterUI.POINT_CENTER(parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new PutPathElementInList(parent, elementType, oldNameOfElement, elementName.getText()).execute();
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
