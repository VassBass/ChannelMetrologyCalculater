package service.channel.search;

import model.ui.LoadingDialog;
import repository.repos.channel.SearchParams;
import service.channel.list.ChannelListManager;
import service.channel.search.ui.ChannelSearchContext;
import service.channel.search.ui.DatePanel;
import service.channel.search.ui.LocationPanel;
import service.channel.search.ui.swing.SwingChannelSearchDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SwingChannelSearchManager implements ChannelSearchManager {

    private final ChannelListManager parentManager;
    private final ChannelSearchContext context;

    private SwingChannelSearchDialog dialog;

    public void registerDialog(SwingChannelSearchDialog dialog) {
        this.dialog = dialog;
    }

    public SwingChannelSearchManager(@Nonnull ChannelListManager parentManager,
                                     @Nonnull ChannelSearchContext context) {
        this.parentManager = parentManager;
        this.context = context;
    }

    @Override
    public void clickStart() {
        DatePanel datePanel = context.getElement(DatePanel.class);
        LocationPanel locationPanel = context.getElement(LocationPanel.class);

        SearchParams params = new SearchParams();
        params.month = datePanel.getMonth();
        params.year = datePanel.getYear();
        params.locationZone = locationPanel.getSearchingZone();
        params.locationValue = locationPanel.getSearchingValue();
        SearchParams.SEARCH_BUFFER = params;

        new Worker().start();
    }

    @Override
    public void clickCancel() {
        SearchParams.SEARCH_BUFFER = null;
        new Worker().start();
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    private final class Worker extends SwingWorker<Boolean, Void> {

        private LoadingDialog loadingDialog;

        void start() {
            loadingDialog = new LoadingDialog(dialog);
            loadingDialog.showing();
            this.execute();
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            parentManager.revaluateChannelTable();
            return true;
        }

        @Override
        protected void done() {
            loadingDialog.shutdown();
            try {
                if (get()) {
                    dialog.shutdown();
                } else showErrorMessage();
            } catch (InterruptedException | ExecutionException e) {
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            JOptionPane.showMessageDialog(dialog, "Виникла помилка", "Помилка", JOptionPane.ERROR_MESSAGE);
            SearchParams.SEARCH_BUFFER = null;
        }
    }
}
