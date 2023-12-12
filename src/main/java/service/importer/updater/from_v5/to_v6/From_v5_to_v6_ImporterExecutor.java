package service.importer.updater.from_v5.to_v6;

import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.importer.ImportOption;
import service.importer.Importer;
import service.importer.model.ModelHolder;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class From_v5_to_v6_ImporterExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(From_v5_to_v6_ImporterExecutor.class);

    private static final String QUESTION = "question";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public From_v5_to_v6_ImporterExecutor(@Nonnull ApplicationScreen applicationScreen,
                                          @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        Map<String, String> labels = Labels.getRootLabels();
        Map<String, String> messages = Messages.getMessages(From_v5_to_v6_ImporterExecutor.class);

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(applicationScreen);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            int secondResult = JOptionPane.showConfirmDialog(applicationScreen, messages.get(QUESTION), labels.get(RootLabelName.IMPORTING), JOptionPane.YES_NO_CANCEL_OPTION);
            ImportOption option = null;
            if (secondResult == 0) option = ImportOption.REPLACE_EXISTED;
            if (secondResult == 1) option = ImportOption.IGNORE_EXISTED;
            if (option != null) {
                final Importer importer = new DefaultImporter(option, repositoryFactory);
                final LoadingDialog loadDialog = new LoadingDialog(applicationScreen);
                loadDialog.showing();
                new SwingWorker<Boolean, Void>() {
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
                            String message = success ? messages.get(SUCCESS) : messages.get(ERROR);
                            int messageType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                            JOptionPane.showMessageDialog(applicationScreen, message, labels.get(RootLabelName.IMPORTING), messageType);
                            System.exit(0);
                        } catch (InterruptedException | ExecutionException e) {
                            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
                            JOptionPane.showMessageDialog(applicationScreen, messages.get(ERROR), labels.get(RootLabelName.IMPORTING), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        }
    }
}
