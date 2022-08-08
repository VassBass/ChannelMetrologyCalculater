package certificates;

import calculation.Calculation;
import model.Channel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import service.FileBrowser;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Objects;

public abstract class Certificate {
    protected static String YEAR_WORD(double d){
        if (d == 0D){
            return "років";
        }else if (d > 0 && d < 1){
            return "року";
        }else if (d == 1D){
            return "рік";
        }else if (d > 1 && d < 5){
            return "роки";
        }else {
            return "років";
        }
    }

    protected HSSFWorkbook book;
    protected File certificateFile;

    protected Calculation result;
    protected HashMap<Integer, Object> values;
    protected Channel channel;

    protected String numberOfCertificate;
    protected String checkDate;
    protected boolean alarmCheck;
    protected String alarmValue;
    protected String measurementValue;
    protected String numberOfReference;

    protected HSSFCell cell(int row, int column){
        HSSFSheet sheet = this.book.getSheetAt(0);
        HSSFRow Row = sheet.getRow(row);
        return Row.getCell(column);
    }

    public void init(Calculation result, HashMap<Integer, Object> values, Channel channel){
        this.result = result;
        this.values = values;
        this.channel = channel;
    }

    public void formation(){
        this.putCertificateData();
        this.putChannelData();
        this.putCalibratorData();
        this.putPersons();
    }

    public abstract void putCertificateData();
    public abstract void putChannelData();
    public abstract void putSensorData();
    public abstract void putCalibratorData();
    public abstract void putResult();
    public abstract void putPersons();
    protected abstract double[][]measurementValues();

    public void save(){
        String fileName = "№"
                + this.numberOfCertificate +
                " ("
                + this.checkDate
                + ").xls";
        this.certificateFile = FileBrowser.certificateFile(fileName);
        try {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(this.certificateFile));
            this.book.write(out);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void show(){
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.open(this.certificateFile);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void print(){
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                if (desktop.isSupported(Desktop.Action.PRINT)) {
                    desktop.print(this.certificateFile);
                }else {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            int result = JOptionPane.showConfirmDialog(MainScreen.getInstance(),
                                    "Команда \"Друк\" за замовчуванням не зарегестрована в системі.\nВідкрити у программі за замовчуванням?", "Помилка", JOptionPane.OK_CANCEL_OPTION);
                            if (result == 0){
                                show();
                            }
                        }
                    });
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void openInExplorer(){
        Desktop desktop;
        if (Desktop.isDesktopSupported()){
            desktop = Desktop.getDesktop();
            try {
                desktop.open(FileBrowser.DIR_CERTIFICATES);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public File getCertificateFile(){
        return this.certificateFile;
    }
}