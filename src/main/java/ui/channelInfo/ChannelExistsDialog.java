package ui.channelInfo;

import application.Application;
import converters.ConverterUI;
import model.Channel;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChannelExistsDialog extends JDialog {
    private final JDialog parent;
    private final Channel channel;

    private JLabel message, channelName;
    private JButton buttonOpen, buttonClose;

    public ChannelExistsDialog(JDialog parent, Channel channel){
        super(parent, "Пошук", true);

        this.parent = parent;
        this.channel = channel;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        this.message = new JLabel("Канал з кодом \"" + this.channel.getCode() + "\" знайдено:", SwingConstants.CENTER);
        this.channelName = new JLabel("\"" + this.channel.getName() + "\"", SwingConstants.CENTER);
        this.buttonClose = new DefaultButton("Закрити");
        this.buttonOpen = new DefaultButton("Відкрити");
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.buttonClose.addActionListener(clickClose);
        this.buttonOpen.addActionListener(clickOpen);
    }

    private void build(){
        this.setSize(500,120);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        this.setResizable(false);

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickClose = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickOpen = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            parent.dispose();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new DialogChannel(Application.context.mainScreen, channel).setVisible(true);
                }
            });
        }
    };

    private class MainPanel extends JPanel {
        MainPanel(){
            super(new GridBagLayout());
            this.setBackground(Color.WHITE);

            this.add(message, new Cell(0,0,2));
            this.add(channelName, new Cell(0,1,2));
            this.add(buttonClose, new Cell(0,2,1));
            this.add(buttonOpen, new Cell(1,2,1));
        }

        private class Cell extends GridBagConstraints {
            Cell(int x, int y, int width){
                super();

                this.fill = BOTH;
                this.weightx = 1.0;
                this.insets = new Insets(5,0,5,0);

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
