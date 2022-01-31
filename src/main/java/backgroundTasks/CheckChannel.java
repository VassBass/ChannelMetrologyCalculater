package backgroundTasks;

import application.Application;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;

public class CheckChannel extends SwingWorker<Boolean, Void> {
    private final JDialog parent;
    private final String code;
    private final LoadDialog loadWindow;

    public CheckChannel(JDialog parent, String code){
        super();
        this.parent = parent;
        this.code = code;
        this.loadWindow = new LoadDialog(parent);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadWindow.setVisible(true);
            }
        });
    }

    public void start(){
        if (!Application.isBusy(parent)){
            Application.setBusy(true);
            this.execute();
        }
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return Application.context.channelsController.isExist(this.code);
    }

    @Override
    protected void done() {
        Application.setBusy(false);
        this.loadWindow.dispose();
        String CHECK = "Перевірка";
        try {
            String m;
            if (this.get()){
                m = "Канал з данним кодом вже існує в списку";
            }else {
                m = "Канал з данним кодом відсутній в списку";
            }
            JOptionPane.showMessageDialog(this.parent, m, CHECK, JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception ex){
            String m = "Виникла помилка, спробуйте будь-ласка ще раз";
            JOptionPane.showMessageDialog(this.parent, m, CHECK,JOptionPane.ERROR_MESSAGE);
        }
    }
}
