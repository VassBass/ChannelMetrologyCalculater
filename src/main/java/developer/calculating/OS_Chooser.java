package developer.calculating;

import converters.ConverterUI;
import model.Channel;
import service.SystemData;
import ui.calculate.start.CalculateStartDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OS_Chooser extends JDialog {
    private static final String CERTIFICATE = "Сертифікат/Протокол";
    private static final String WINDOWS = "Windows";
    private static final String UNIX = "Unix";
    private static final String BEGIN = "Почати (Alt + Enter)";
    private static final String CANCEL = "Відміна (Esc)";

    private final MainScreen mainScreen;
    private final Channel channel;

    private JComboBox<String>osList;
    private JButton positiveButton, negativeButton;

    public OS_Chooser(MainScreen mainScreen, Channel channel){
        super(mainScreen, CERTIFICATE, true);
        this.mainScreen = mainScreen;
        this.channel = channel;

        createModels();
        createView();
        addControllers();
    }

    private void createModels(){
        this.osList = new JComboBox<>(new String[]{WINDOWS, UNIX});
        this.osList.setBackground(Color.WHITE);

        this.positiveButton = new DefaultButton(BEGIN);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void createView(){
        this.setSize(350,100);
        this.setLocation(ConverterUI.POINT_CENTER(mainScreen, this));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(negativeButton);
        buttonsPanel.add(positiveButton);

        mainPanel.add(osList);
        mainPanel.add(buttonsPanel);
        this.setContentPane(mainPanel);
    }

    private void addControllers(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        negativeButton.addActionListener(clickNegativeButton);
        positiveButton.addActionListener(clickPositiveButton);

        osList.addKeyListener(keyListener);
        negativeButton.addKeyListener(keyListener);
        positiveButton.addKeyListener(keyListener);
    }

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (channel != null && osList.getSelectedItem() != null){
                SystemData os = osList.getSelectedItem().toString().equals(WINDOWS) ? SystemData.SYS_WINDOWS : SystemData.SYS_UNIX;
                new CalculateStartDialog(mainScreen, channel, null, os).setVisible(true);
            }
            dispose();
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_ESCAPE:
                    negativeButton.doClick();
                    break;
                case KeyEvent.VK_ENTER:
                    if (e.isAltDown()) positiveButton.doClick();
                    break;
            }
        }
    };
}
