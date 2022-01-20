package ui.pathLists;

import application.Application;
import converters.ConverterUI;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String CLEAR = "Очистити";
    private static final String CANCEL = "Відміна";
    private String message(String elementType){
        return  "Ви впевнені що хочете очистити "
                + elementType
                + "?";
    }

    private final PathListsDialog dialog;
    private final JDialog current;
    private final String elementType;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    public ConfirmDialog(PathListsDialog dialog, String elementType){
        super(dialog, REMOVE, true);
        this.dialog = dialog;
        this.current = this;
        this.elementType = elementType;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.message = new JLabel(this.message(this.elementType));

        this.positiveButton = new DefaultButton(CLEAR);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.negativeButton.addActionListener(this.clickCancel);
        this.positiveButton.addActionListener(this.clickRemove);
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(current)) return;
            dispose();
            switch (elementType){
                case PathListsTable.DEPARTMENTS_LIST:
                    Application.context.departmentsController.clear();
                    break;
                case PathListsTable.AREAS_LIST:
                    Application.context.areasController.clear();
                    break;
                case PathListsTable.PROCESSES_LIST:
                    Application.context.processesController.clear();
                    break;
                case PathListsTable.INSTALLATIONS_LIST:
                    Application.context.installationsController.clear();
                    break;
            }
            dialog.update(elementType);
        }
    };

    private void build() {
        this.setSize(450,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.dialog,this));

        this.setContentPane(new MainPanel());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0,2));
            this.add(negativeButton, new Cell(0,1,1));
            this.add(positiveButton, new Cell(1,1,1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width){
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