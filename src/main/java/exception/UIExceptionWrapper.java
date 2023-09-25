package exception;

import model.ui.DefaultButton;
import model.ui.DefaultLabel;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceExecutor;

import javax.swing.*;

public class UIExceptionWrapper implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(UIExceptionWrapper.class);

    private static final String EXCEPTION_LOG_MESSAGE = "Exception was thrown!";
    private static final String UNEXPECTED_EXCEPTION_MESSAGE = "Виникла невідома помилка...зверніться в тех.підтримку.";

    private final Runnable application;
    private final ErrorDialog dialog;

    public UIExceptionWrapper(Runnable application) {
        this.application = application;
        dialog = new ErrorDialog();
    }


    @Override
    public void execute() {
        try {
            application.run();
        } catch (ValidationException ve) {
            logger.warn(EXCEPTION_LOG_MESSAGE, ve);
            dialog.showWithMessage(ve.getUiMessage());
        } catch (Exception e) {
            logger.warn(EXCEPTION_LOG_MESSAGE, e);
            dialog.showWithMessage(UNEXPECTED_EXCEPTION_MESSAGE);
        }
    }

    private static class ErrorDialog extends JDialog {
        private static final String TITLE = "Помилка";
        private static final String CLOSE = "Закрити";

        private final DefaultLabel message;

        private ErrorDialog() {
            super();
            setTitle(TITLE);
            setAlwaysOnTop(true);

            message = new DefaultLabel();
            DefaultButton button = new DefaultButton(CLOSE);
            button.addActionListener(e -> setVisible(false));

            DefaultPanel panel = new DefaultPanel();
            panel.add(message, new CellBuilder().y(0).build());
            panel.add(button, new CellBuilder().y(1).build());

            setContentPane(panel);
        }

        private void showWithMessage(String message) {
            this.message.setText(message);
            setVisible(true);
        }
    }
}
