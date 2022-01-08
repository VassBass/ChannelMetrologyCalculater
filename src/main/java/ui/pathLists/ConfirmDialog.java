package ui.pathLists;

import backgroundTasks.controllers.RemovePathElements;
import constants.Strings;
import converters.ConverterUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmDialog extends JDialog implements UI_Container {
    private final PathListsDialog dialog;
    private final String elementType;

    private JLabel message;
    private JButton positiveButton, negativeButton;

    private final Color buttonsColor = new Color(51,51,51);

    public ConfirmDialog(PathListsDialog dialog, String elementType){
        super(dialog, Strings.REMOVE, true);
        this.dialog = dialog;
        this.elementType = elementType;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        String m = "Ви впевнені що хочете очистити "
                + elementType
                + "?";
        this.message = new JLabel(m);

        this.positiveButton = new JButton(Strings.CLEAR);
        this.positiveButton.setBackground(buttonsColor);
        this.positiveButton.setForeground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(buttonsColor);
        this.negativeButton.setForeground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.positiveButton.addChangeListener(pushButton);
        this.negativeButton.addChangeListener(pushButton);

        this.negativeButton.addActionListener(clickCancel);
        this.positiveButton.addActionListener(clickRemove);
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

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new RemovePathElements(dialog, elementType, null).execute();
        }
    };

    @Override
    public void build() {
        this.setSize(450,100);
        this.setLocation(ConverterUI.POINT_CENTER(dialog,this));

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
