package ui.calculate.end;

import application.Application;
import backgroundTasks.CertificateFormation;
import calculation.Calculation;
import certificates.Certificate;
import converters.ConverterUI;
import model.Channel;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class CalculateEndDialog extends JDialog {
    public static final String SUCCESS_SAVE_CERTIFICATE = "Сертифікат та протокол успішно сформовані";
    public static final String PRINT = "Друкувати";
    public static final String OPEN = "Відкрити";
    public static final String OPEN_IN_EXPLORER = "Відкрити папку";
    public static final String ERROR_SAVE_CERTIFICATE = "Сертифікат не вдалося сформувати";
    public static final String TRY_AGAIN = "Повторити";
    public static final String FINISH = "Завершити";
    public static final String SUCCESS = "Успіх";
    public static final String ERROR = "Помилка";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;
    private final Certificate certificate;

    private JLabel message;
    private JButton buttonPrint, buttonOpen, buttonOpenInExplorer, buttonFinish, buttonTryAgain;

    public CalculateEndDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation, Certificate certificate){
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

    private void createElements() {
        if (this.certificate.getCertificateFile().exists()){
            this.message = new JLabel(SUCCESS_SAVE_CERTIFICATE);
            this.buttonPrint = new DefaultButton(PRINT);
            this.buttonOpen = new DefaultButton(OPEN);
            this.buttonOpenInExplorer = new DefaultButton(OPEN_IN_EXPLORER);
        }else {
            this.message = new JLabel(ERROR_SAVE_CERTIFICATE);
            this.buttonTryAgain = new DefaultButton(TRY_AGAIN);
        }
        this.buttonFinish = new DefaultButton(FINISH);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        if (this.certificate.getCertificateFile().exists()) {
            this.buttonPrint.addActionListener(this.clickPrint);
            this.buttonOpen.addActionListener(this.clickOpen);
            this.buttonOpenInExplorer.addActionListener(this.clickOpenInExplorer);
        }else {
            this.buttonTryAgain.addActionListener(this.clickTryAgain);
        }
        this.buttonFinish.addActionListener(this.clickFinish);
    }

    private void build() {
        this.setSize(400,180);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel(this.certificate.getCertificateFile().exists()));
    }

    private static String title(File file){
        if (file.exists()){
            return SUCCESS;
        }else {
            return ERROR;
        }
    }

    private final ActionListener clickPrint = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.print();
            dispose();
            setChannelList();
        }
    };

    private final ActionListener clickOpen = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.show();
            dispose();
            setChannelList();
        }
    };

    private final ActionListener clickOpenInExplorer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            certificate.openInExplorer();
            dispose();
            setChannelList();
        }
    };

    private final ActionListener clickFinish = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            setChannelList();
        }
    };

    private final ActionListener clickTryAgain = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CertificateFormation(mainScreen, channel, values, calculation).execute();
        }
    };

    private void setChannelList(){
        if (Application.context.channelSorter.isOn()){
            mainScreen.setChannelsList(Application.context.channelSorter.getCurrent());
        }else {
            mainScreen.setChannelsList(Application.context.channelService.getAll());
        }
    }

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