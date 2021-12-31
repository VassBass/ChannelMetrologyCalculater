import controller.FileBrowser;
import support.Default;
import support.Lists;
import support.Settings;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;

public class StartApp {

    public static void main(String[]args){
        final LoadDialog loadDialog = new LoadDialog();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    loadDialog.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        new Init(loadDialog).execute();
    }

    public static class Init extends SwingWorker<Void, Void> {

        private final LoadDialog loadDialog;

        protected Init(LoadDialog loadDialog){
            super();
            this.loadDialog = loadDialog;
        }

        protected Void doInBackground() throws Exception
        {
            FileBrowser.init();
            Lists.create();
            Settings.checkSettings();
            return null;
        }

        protected void done() {
            try {
                final MainScreen mainScreen = new MainScreen(Lists.channels());
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        loadDialog.dispose();
                        mainScreen.setVisible(true);
                    }
                });
                Default.loadForms();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
