package ui.pathLists;

import constants.Strings;
import support.Converter;
import support.Lists;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PathElementsRemove extends JDialog implements UI_Container {
    private final PathListsDialog parent;
    private final String elementType, elementName;

    private String[]elements;

    private JComboBox<String>elementList = null;
    private JLabel removingElement;
    private JButton buttonCancel, buttonRemoveAll, buttonRemove;

    private final Color buttonsColor = new Color(51,51,51);

    public PathElementsRemove(PathListsDialog parent, String elementType, String elementName){
        super(parent, Strings.REMOVE + " \"" + elementType + "\"", true);
        this.parent = parent;
        this.elementType = elementType;
        this.elementName = elementName;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        switch (elementType){
            case Strings.DEPARTMENTS_LIST:
                elements = Objects.requireNonNull(Lists.departments()).toArray(new String[0]);
                break;
            case Strings.AREAS_LIST:
                elements = Objects.requireNonNull(Lists.areas()).toArray(new String[0]);
                break;
            case Strings.PROCESSES_LIST:
                elements = Objects.requireNonNull(Lists.processes()).toArray(new String[0]);
                break;
            case Strings.INSTALLATIONS_LIST:
                elements = Objects.requireNonNull(Lists.installations()).toArray(new String[0]);
                break;
        }
        if (elementName == null){
            this.elementList = new JComboBox<>(Objects.requireNonNull(elements));
        }else {
            this.removingElement = new JLabel(Strings.REMOVE
                    + " \""
                    + elementName
                    + "\""
                    + "?");
        }

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonRemoveAll = new JButton(Strings.REMOVE_ALL);
        this.buttonRemoveAll.setBackground(buttonsColor);
        this.buttonRemoveAll.setForeground(Color.white);
        this.buttonRemoveAll.setFocusPainted(false);
        this.buttonRemoveAll.setContentAreaFilled(false);
        this.buttonRemoveAll.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(buttonsColor);
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);
    }

    @Override
    public void setReactions() {

    }

    @Override
    public void build() {
        this.setSize(400,100);
        this.setLocation(Converter.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            if (elementName == null){
                this.add(elementList, new Cell(0,0,3));
            }else {
                this.add(removingElement, new Cell(0,0,3));
            }
            this.add(buttonCancel, new Cell(0,1,1));
            this.add(buttonRemoveAll, new Cell(1,1,1));
            this.add(buttonRemove, new Cell(2,1,1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell (int x, int y, int width){
                super();

                this.fill = BOTH;
                this.weightx = 1.0;
                this.weighty = 1.0;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
