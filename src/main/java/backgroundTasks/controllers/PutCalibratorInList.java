package backgroundTasks.controllers;

import constants.Strings;
import support.Calibrator;
import support.Lists;
import ui.LoadDialog;
import ui.calibratorsList.CalibratorsListDialog;
import ui.calibratorsList.calibratorInfo.CalibratorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class PutCalibratorInList extends SwingWorker<Boolean, Void> {
    private final CalibratorsListDialog mainDialog;
    private final CalibratorInfoDialog dialog;
    private final Calibrator newCalibrator, oldCalibrator;
    private final LoadDialog loadDialog;


    public PutCalibratorInList(CalibratorsListDialog mainDialog, CalibratorInfoDialog dialog,
                               Calibrator newCalibrator, Calibrator oldCalibrator){
        super();
        this.mainDialog = mainDialog;
        this.dialog = dialog;
        this.newCalibrator = newCalibrator;
        this.oldCalibrator = oldCalibrator;
        this.loadDialog = new LoadDialog(dialog);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    /*return:   true - калибратор успешно изменен/добавлен
                false - калибратор с таким названием(newCalibrator.name) уже существует в списке
     */
    @Override
    protected Boolean doInBackground() throws Exception {
        ArrayList<Calibrator> calibrators = Lists.calibrators();
        if (this.oldCalibrator == null){
            for (Calibrator calibrator : Objects.requireNonNull(calibrators)){
                if (calibrator.getName().equals(this.newCalibrator.getName())){
                    return false;
                }
            }
            calibrators.add(this.newCalibrator);
        }else {
            int oldIndex = -1;
            for (int x = 0; x < Objects.requireNonNull(calibrators).size(); x++){
                if (calibrators.get(x).getName().equals(this.oldCalibrator.getName())){
                    oldIndex = x;
                    break;
                }
            }
            if (this.oldCalibrator.getName().equals(Strings.CALIBRATOR_FLUKE718_30G)){
                Calibrator calibrator = Objects.requireNonNull(Lists.calibrators()).get(oldIndex);
                calibrator.setNumber(this.newCalibrator.getNumber());
                calibrator.setErrorFormula(this.newCalibrator.getErrorFormula());
                calibrator.setCertificateName(this.newCalibrator.getCertificateName());
                calibrator.setCertificateDate(this.newCalibrator.getCertificateDate());
                calibrator.setCertificateCompany(this.newCalibrator.getCertificateCompany());
                calibrators.set(oldIndex, calibrator);
            }else {
                for (int x = 0; x < calibrators.size(); x++) {
                    if (calibrators.get(x).getName().equals(this.newCalibrator.getName()) && x != oldIndex) {
                        return false;
                    }
                }
                calibrators.set(oldIndex, this.newCalibrator);
            }
        }
        Lists.saveCalibratorsListToFile(calibrators);
        return true;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.dispose();
                String m;
                if (this.oldCalibrator == null){
                    m = "Калібратор успішно додано до списку!";
                }else {
                    m = "Калібратор успішно змінено!";
                }
                JOptionPane.showMessageDialog(this.mainDialog, m, Strings.SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                mainDialog.mainTable.update();
            }else {
                String m = "Калібратор з такою назвою вже існує в списку!";
                JOptionPane.showMessageDialog(this.mainDialog, m, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String er = "Відбулася помилка! Спробуйте будь-ласка ще раз.";
            JOptionPane.showMessageDialog(dialog, er, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
