package ui.channelInfo;

import model.Channel;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static ui.UI_Constants.POINT_CENTER;

public class DialogChannelExists extends JDialog {
    private final Window parent;
    private final Channel channel;

    private JLabel message, channelName;
    private JButton buttonOpen, buttonClose;

    public DialogChannelExists(Window parent, Channel channel){
        super(parent, "Пошук", ModalityType.APPLICATION_MODAL);

        this.parent = parent;
        this.channel = channel;

        this.createElements();
        this.build();
        this.setReactions();
    }

    private void createElements(){
        this.message = new JLabel("Канал з кодом \"" + this.channel.getCode() + "\" знайдено:", SwingConstants.CENTER);
        this.channelName = new JLabel("\"" + this.channel.getName() + "\"", SwingConstants.CENTER);
        this.buttonClose = new DefaultButton("Закрити (Esc)");
        this.buttonOpen = new DefaultButton("Відкрити (Enter)");
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.buttonClose.addActionListener(clickClose);
        this.buttonOpen.addActionListener(clickOpen);

        this.buttonOpen.addKeyListener(keyListener);
        this.buttonClose.addKeyListener(keyListener);
    }

    private void build(){
        this.setSize(500,120);
        this.setLocation(POINT_CENTER(this.parent, this));
        this.setResizable(false);

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickClose = e -> dispose();

    private final ActionListener clickOpen = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            if (parent instanceof JDialog) parent.dispose();
            EventQueue.invokeLater(() -> new DialogChannel(channel).setVisible(true));
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_ENTER:
                    buttonOpen.doClick();
                    break;
                case KeyEvent.VK_ESCAPE:
                    buttonClose.doClick();
                    break;
            }
        }
    };

    private class MainPanel extends JPanel {
        MainPanel(){
            super(new GridBagLayout());
            this.setBackground(Color.WHITE);

            this.add(message, new MainPanel.Cell(0,0,2));
            this.add(channelName, new MainPanel.Cell(0,1,2));
            this.add(buttonClose, new MainPanel.Cell(0,2,1));
            this.add(buttonOpen, new MainPanel.Cell(1,2,1));
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
