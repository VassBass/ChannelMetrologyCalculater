package ui.calculate.end;

import backgroundTasks.CertificateFormation;
import measurements.calculation.Calculation;
import measurements.certificates.Certificate;
import converters.ConverterUI;
import support.Channel;
import support.Lists;
import constants.Strings;
import support.Values;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CalculateEndDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;
    private final Calculation calculation;
    private final Certificate certificate;

    private JLabel message;
    private JButton buttonPrint, buttonOpen, buttonOpenInExplorer, buttonFinish, buttonTryAgain;

    public CalculateEndDialog(MainScreen mainScreen, Channel channel, Values values, Calculation calculation, Certificate certificate){
        super(mainScreen, title(certificate.getCertificateFile()), true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;
        this.certificate = certificate;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        if (this.certificate.getCertificateFile().exists()){
            this.message = new JLabel(Strings.SUCCESS_SAVE_CERTIFICATE);

            this.buttonPrint = new JButton(Strings.PRINT);
            this.buttonPrint.setBackground(Color.white);
            this.buttonPrint.setFocusPainted(false);
            this.buttonPrint.setContentAreaFilled(false);
            this.buttonPrint.setOpaque(true);

            this.buttonOpen = new JButton(Strings.OPEN);
            this.buttonOpen.setBackground(Color.white);
            this.buttonOpen.setFocusPainted(false);
            this.buttonOpen.setContentAreaFilled(false);
            this.buttonOpen.setOpaque(true);

            this.buttonOpenInExplorer = new JButton(Strings.OPEN_IN_EXPLORER);
            this.buttonOpenInExplorer.setBackground(Color.white);
            this.buttonOpenInExplorer.setFocusPainted(false);
            this.buttonOpenInExplorer.setContentAreaFilled(false);
            this.buttonOpenInExplorer.setOpaque(true);
        }else {
            this.message = new JLabel(Strings.ERROR_SAVE_CERTIFICATE);

            this.buttonTryAgain = new JButton(Strings.TRY_AGAIN);
            this.buttonTryAgain.setBackground(Color.white);
            this.buttonTryAgain.setFocusPainted(false);
            this.buttonTryAgain.setContentAreaFilled(false);
            this.buttonTryAgain.setOpaque(true);
        }

        this.buttonFinish = new JButton(Strings.FINISH);
        this.buttonFinish.setBackground(Color.white);
        this.buttonFinish.setFocusPainted(false);
        this.buttonFinish.setContentAreaFilled(false);
        this.buttonFinish.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        if (this.certificate.getCertificateFile().exists()) {
            this.buttonPrint.addChangeListener(this.pushButton);
            this.buttonOpen.addChangeListener(this.pushButton);
            this.buttonOpenInExplorer.addChangeListener(this.pushButton);

            this.buttonPrint.addActionListener(this.clickPrint);
            this.buttonOpen.addActionListener(this.clickOpen);
            this.buttonOpenInExplorer.addActionListener(this.clickOpenInExplorer);
        }else {
            this.buttonTryAgain.addChangeListener(this.pushButton);
            this.buttonTryAgain.addActionListener(this.clickTryAgain);
        }

        this.buttonFinish.addChangeListener(this.pushButton);
        this.buttonFinish.addActionListener(this.clickFinish);
    }

    @Override
    public void build() {
        this.setSize(400,180);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel(this.certificate.getCertificateFile().exists()));
    }

    private static String title(File file){
        if (file.exists()){
            return Strings.SUCCESS;
        }else {
            return Strings.ERROR;
        }
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(Color.white.darker());
            }else {
                button.setBackground(Color.white);
            }
        }
    };

    private final ActionListener clickPrint = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.print();
            dispose();
            mainScreen.update(Lists.channels(),false, null,null);
        }
    };

    private final ActionListener clickOpen = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.show();
            dispose();
            mainScreen.update(Lists.channels(),false, null,null);
        }
    };

    private final ActionListener clickOpenInExplorer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.openInExplorer();
            dispose();
            mainScreen.update(Lists.channels(), false, null, null);
        }
    };

    private final ActionListener clickFinish = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            mainScreen.update(Lists.channels(), false, null, null);
        }
    };

    private final ActionListener clickTryAgain = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CertificateFormation(mainScreen, channel, values, calculation).execute();
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(boolean success){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0,2));

            if (success){
                this.add(buttonPrint, new Cell(0,1,2));
                this.add(buttonOpen, new Cell(0,2,1));
                this.add(buttonOpenInExplorer, new Cell(1,2,1));
                this.add(buttonFinish, new Cell(0,3,2));
            }else{
                this.add(buttonTryAgain, new Cell(0,1,1));
                this.add(buttonFinish, new Cell(1,1,1));
            }
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int x, int y, int width){
                super();

                this.fill = BOTH;
                this.insets = new Insets(2,2,2,2);

                this.gridwidth = width;
                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
