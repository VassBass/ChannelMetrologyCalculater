package ui.pathLists;

import converters.ConverterUI;
import model.Model;
import service.impl.AreaServiceImpl;
import service.impl.DepartmentServiceImpl;
import service.impl.InstallationServiceImpl;
import service.impl.ProcessServiceImpl;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String CLEAR = "Очистити";
    private static final String CANCEL = "Відміна";
    private static final String DEPARTMENTS_LIST = "Список цехів";
    private static final String AREAS_LIST = "Список ділянок";
    private static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    private static final String INSTALLATIONS_LIST = "Список установок";

    private String message(String elementType){
        return  "Ви впевнені що хочете очистити "
                + elementType
                + "?";
    }

    private final PathListsDialog dialog;
    private final Model model;
    private static String elementType(Model model){
        switch (model){
            case DEPARTMENT:
                return DEPARTMENTS_LIST;
            case AREA:
                return AREAS_LIST;
            case PROCESS:
                return PROCESSES_LIST;
            case INSTALLATION:
                return INSTALLATIONS_LIST;
            default:
                return null;
        }
    }

    private JLabel message;
    private JButton positiveButton, negativeButton;

    public ConfirmDialog(PathListsDialog dialog, Model model){
        super(dialog, REMOVE, true);
        this.dialog = dialog;
        this.model = model;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.message = new JLabel(this.message(elementType(this.model)));

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
            dispose();
            switch (model){
                case DEPARTMENT:
                    DepartmentServiceImpl.getInstance().clear();
                    break;
                case AREA:
                    AreaServiceImpl.getInstance().clear();
                    break;
                case PROCESS:
                    ProcessServiceImpl.getInstance().clear();
                    break;
                case INSTALLATION:
                    InstallationServiceImpl.getInstance().clear();
                    break;
            }
            dialog.update(model);
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