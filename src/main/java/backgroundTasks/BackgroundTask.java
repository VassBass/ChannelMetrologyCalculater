package backgroundTasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

/**
 * Wrapper for SwingWorker
 * @see SwingWorker
 */
public abstract class BackgroundTask extends SwingWorker<Boolean, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundTask.class);

    private final LoadDialog loadDialog;

    public BackgroundTask(JDialog window){
        super();
        loadDialog = new LoadDialog(window);
    }

    public BackgroundTask(JFrame window){
        super();
        loadDialog = new LoadDialog(window);
    }

    public abstract void backgroundTask();

    public void startTask(){
        LOGGER.debug("Background task is starting");
        beforeTask();
        this.execute();
    }

    public void beforeTask(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    public void afterTask(){
        loadDialog.dispose();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            backgroundTask();
            return true;
        }catch (Exception e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    protected void done() {
        try {
            if (this.get()){
                afterTask();
                LOGGER.debug("Background task was ends successful");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
