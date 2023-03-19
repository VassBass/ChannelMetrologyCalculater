package service.importer.updater.from_v5.to_v6;

import application.ApplicationScreen;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.importer.ImportOption;
import service.importer.Importer;
import service.importer.model.ModelHolder;
import service.root.ServiceExecuter;
import util.ScreenPoint;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class From_v5_to_v6_ImporterExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(From_v5_to_v6_ImporterExecuter.class);

    private static final String HEADER = "Імпорт";
    private static final String QUESTION_TEXT = "Чи заміняти існуючі дані імпортованими у разі винекнення конфліктів?";
    private static final String SUCCESS_TEXT = "Дані вдало імпортовані. Щоб данні збереглися программа буде закрита.";
    private static final String ERROR_TEXT = "При імпорті виникла помилка. Будь ласка спробуйте ще раз.";

    @Override
    public void execute() {
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();

        if (applicationScreen != null && repositoryFactory != null) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(applicationScreen);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                int secondResult = JOptionPane.showConfirmDialog(applicationScreen, QUESTION_TEXT, HEADER, JOptionPane.YES_NO_CANCEL_OPTION);
                ImportOption option = null;
                if (secondResult == 0) option = ImportOption.REPLACE_EXISTED;
                if (secondResult == 1) option = ImportOption.IGNORE_EXISTED;
                if (option != null) {
                    final Importer importer = new DefaultImporter(option, repositoryFactory);
                    LoadingDialog dialog = LoadingDialog.getInstance();
                    final DialogWrapper loadDialog = new DialogWrapper(applicationScreen, dialog, ScreenPoint.center(applicationScreen, dialog));
                    loadDialog.showing();
                    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() {
                            List<ModelHolder> in = new SqliteReaderOfv5().read(selectedFile);
                            return importer.importing(in);
                        }

                        @Override
                        protected void done() {
                            loadDialog.shutdown();
                            try {
                                boolean success = get();
                                String message = success ? SUCCESS_TEXT : ERROR_TEXT;
                                int messageType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                                JOptionPane.showMessageDialog(applicationScreen, message, HEADER, messageType);
                                System.exit(0);
                            } catch (InterruptedException | ExecutionException e) {
                                logger.warn("Exception was thrown", e);
                                JOptionPane.showMessageDialog(applicationScreen, ERROR_TEXT, HEADER, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };
                    worker.execute();
                }
            }
        }
    }
}
