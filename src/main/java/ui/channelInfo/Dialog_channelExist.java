package ui.channelInfo;

import support.Converter;
import ui.UI_Container;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Dialog_channelExist extends JDialog implements UI_Container {
    private final JDialog current;
    private final JDialog parent;

    private JButton okButton;

    public Dialog_channelExist(JDialog parent){
        super();
        this.current = this;
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.okButton = new JButton("Канал с таким кодом уже есть в списке!");
        this.okButton.setForeground(Color.red);
        this.okButton.setBackground(Color.white);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
        this.okButton.addActionListener(clickOk);
        this.okButton.addChangeListener(pushButton);
    }

    @Override
    public void build() {
        this.setSize(500, 60);
        this.setLocation(Converter.POINT_CENTER(parent, this));
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        this.add(this.okButton);
    }

    private final ActionListener clickOk = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
            parent.setVisible(true);
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                setBackground(Color.white.darker());
            }else {
                setBackground(Color.white);
            }
        }
    };

    private final WindowListener windowListener = new WindowListener() {
        @Override public void windowOpened(WindowEvent e) {}
        @Override public void windowIconified(WindowEvent e) {}
        @Override public void windowDeiconified(WindowEvent e) {}
        @Override public void windowActivated(WindowEvent e) {}
        @Override public void windowDeactivated(WindowEvent e) {}
        @Override public void windowClosed(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            parent.setVisible(true);
            current.dispose();
        }

    };
}
