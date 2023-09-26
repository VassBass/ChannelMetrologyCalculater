package service.channel.list;

import application.ApplicationScreen;
import model.dto.Channel;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.channel.SearchParams;
import repository.repos.sensor.SensorRepository;
import service.calculation.CalculationExecuter;
import service.channel.info.SwingChannelInfoExecuter;
import service.channel.list.ui.ChannelListInfoTable;
import service.channel.list.ui.ChannelListSearchPanel;
import service.channel.list.ui.ChannelListSwingContext;
import service.channel.list.ui.ChannelListTable;
import service.channel.search.ChannelSearchExecutor;
import util.DateHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SwingChannelListManager implements ChannelListManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListManager.class);

    private static final String SUCCESS = "Успіх";
    private static final String ERROR = "Помилка";
    private static final String REMOVE_QUESTION_TITLE_FORM = "Видалити: \"%s\"";
    private static final String REMOVE_QUESTION_FORM = "Ви впевнені що хочете видалити канал: \"%s\"";
    private static final String REMOVE_SUCCESS_MESSAGE = "Канал був успішно видалений";
    private static final String EXCEPTION_MESSAGE = "Exception was thrown";
    private static final String ERROR_MESSAGE = "Виникла помилка, будь ласка спробуйте ще раз";
    private static final String SEARCH_SUCCESS_MESSAGE_FORM = "Канал з кодом \"%s\" було знайдено: \"%s\". Відкрити про ньго інформацію?";
    private static final String SEARCH_SUCCESS_TITLE = "Знайдено";
    private static final String SEARCH_FAIL_MESSAGE_FORM = "Канал з кодом \"%s\" не знайдено в базі.";
    private static final String SEARCH_FAIL_TITLE = "Не знайдено";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final ChannelListConfigHolder configHolder;
    private final ChannelListSwingContext context;

    public SwingChannelListManager(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull ChannelListConfigHolder configHolder,
                                   @Nonnull ChannelListSwingContext context) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.context = context;
    }

    @Override
    public void channelSelected(String channelCode) {
        ChannelListInfoTable channelInfoTable = context.getElement(ChannelListInfoTable.class);

        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        channelInfoTable.updateInfo(channelRepository.get(channelCode));
    }

    @Override
    public void addChannel() {
        new SwingChannelInfoExecuter(applicationScreen, repositoryFactory, this).execute();
    }

    @Override
    public void showChannelInfo() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        String channelCode = channelListTable.getSelectedChannelCode();
        if (channelCode != null) {
            Channel channel = channelRepository.get(channelCode);
            if (channel != null) {
                new SwingChannelInfoExecuter(applicationScreen, repositoryFactory, this)
                        .registerChannel(channel)
                        .execute();
            }
        }
    }

    @Override
    public void removeChannel() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);

        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        String channelCode = channelListTable.getSelectedChannelCode();
        if (channelCode != null) {
            Channel channel = channelRepository.get(channelCode);
            if (channel != null) {
                String message = String.format(REMOVE_QUESTION_FORM, channel.getName());
                String title = String.format(REMOVE_QUESTION_TITLE_FORM, channelCode);
                int result = JOptionPane.showConfirmDialog(applicationScreen, message, title, JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    LoadingDialog loadingDialog = new LoadingDialog(applicationScreen);
                    loadingDialog.showing();
                    new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() {
                            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
                            return channelRepository.remove(channel) && sensorRepository.removeByChannelCode(channel.getCode());
                        }

                        @Override
                        protected void done() {
                            loadingDialog.shutdown();
                            try {
                                if (get()) {
                                    JOptionPane.showMessageDialog(applicationScreen, REMOVE_SUCCESS_MESSAGE, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                                } else errorReaction(null);
                            } catch (InterruptedException | ExecutionException e) {
                                errorReaction(e);
                            }
                            revaluateChannelTable();
                        }

                        private void errorReaction(Exception e) {
                            logger.warn(EXCEPTION_MESSAGE, e);
                            JOptionPane.showMessageDialog(applicationScreen, ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
                        }
                    }.execute();
                }
            }
        }
    }

    @Override
    public void calculateChannel() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        String channelCode = channelListTable.getSelectedChannelCode();
        if (channelCode != null) {
            Channel channel = channelRepository.get(channelCode);
            if (channel != null) {
                new CalculationExecuter(applicationScreen, repositoryFactory, channel).execute();
            }
        }
    }

    @Override
    public void calculateChannel(@Nonnull Channel channel) {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        if (channelRepository.isExist(channel.getCode())) {
            new CalculationExecuter(applicationScreen, repositoryFactory, channel).execute();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void openChannelCertificateFolder() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                File certificateFolder = new File(configHolder.getChannelsCertificatesFolder());
                if (!certificateFolder.exists()) certificateFolder.mkdirs();
                desktop.open(certificateFolder);
            } catch (Exception ex) {
                logger.warn(EXCEPTION_MESSAGE, ex);
            }
        }
    }

    @Override
    public void search() {
        ChannelListSearchPanel searchPanel = context.getElement(ChannelListSearchPanel.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        String channelCode = searchPanel.getChannelCode();
        Channel channel = channelRepository.get(channelCode);
        if (Objects.nonNull(channel)) {
            String message = String.format(SEARCH_SUCCESS_MESSAGE_FORM, channelCode, channel.getName());
            int result = JOptionPane.showConfirmDialog(applicationScreen, message, SEARCH_SUCCESS_TITLE, JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                new SwingChannelInfoExecuter(applicationScreen, repositoryFactory, this)
                        .registerChannel(channel)
                        .execute();
            }
        } else {
            String message = String.format(SEARCH_FAIL_MESSAGE_FORM, channelCode);
            JOptionPane.showMessageDialog(applicationScreen, message, SEARCH_FAIL_TITLE, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void advancedSearch() {
        new ChannelSearchExecutor(applicationScreen, this).execute();
    }

    @Override
    public void revaluateChannelTable() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        Collection<Channel> channelList = Objects.isNull(SearchParams.SEARCH_BUFFER) ?
                channelRepository.getAll() :
                channelRepository.search(SearchParams.SEARCH_BUFFER);

        channelListTable.setChannelList(channelList.stream()
                .sorted(Comparator.comparingLong(c -> {
                    String nextDate = DateHelper.getNextDate(c.getDate(), c.getFrequency());
                    if (nextDate.isEmpty()) return Long.MIN_VALUE;

                    Calendar nextDateCal = DateHelper.stringToDate(nextDate);
                    if (Objects.isNull(nextDateCal)) return Long.MIN_VALUE;

                    long nextDateLong = nextDateCal.getTimeInMillis();
                    long currentDate = Calendar.getInstance().getTimeInMillis();
                    return nextDateLong - currentDate;
                }))
                .collect(Collectors.toList()));
    }
}
