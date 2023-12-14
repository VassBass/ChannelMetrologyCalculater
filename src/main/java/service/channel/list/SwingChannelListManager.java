package service.channel.list;

import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.Channel;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.channel.SearchParams;
import repository.repos.sensor.SensorRepository;
import service.calculation.CalculationExecutor;
import service.channel.info.SwingChannelInfoExecutor;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SwingChannelListManager implements ChannelListManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListManager.class);

    private static final String DELETE_CHANNEL_QUESTION = "deleteChannelQuestion";
    private static final String DELETE_CHANNEL_SUCCESS = "deleteChannelSuccess";
    private static final String SEARCH_SUCCESS_QUESTION = "searchSuccessQuestion";
    private static final String SEARCH_FAIL = "searchFail";

    private final Map<String, String> rootLabels;
    private final Map<String, String> rootMessages;
    private final Map<String, String> messages;

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
        rootLabels = Labels.getRootLabels();
        rootMessages = Messages.getRootMessages();
        messages = Messages.getMessages(SwingChannelListManager.class);
    }

    @Override
    public void channelSelected(String channelCode) {
        ChannelListInfoTable channelInfoTable = context.getElement(ChannelListInfoTable.class);

        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        channelInfoTable.updateInfo(channelRepository.get(channelCode));
    }

    @Override
    public void addChannel() {
        new SwingChannelInfoExecutor(applicationScreen, repositoryFactory, this).execute();
    }

    @Override
    public void showChannelInfo() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        String channelCode = channelListTable.getSelectedChannelCode();
        if (channelCode != null) {
            Channel channel = channelRepository.get(channelCode);
            if (channel != null) {
                new SwingChannelInfoExecutor(applicationScreen, repositoryFactory, this)
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
                String message = String.format("%s: \"%s\"", messages.get(DELETE_CHANNEL_QUESTION), channel.getName());
                String title = String.format("%s: \"%s\"", rootLabels.get(RootLabelName.DELETE), channelCode);
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
                                    JOptionPane.showMessageDialog(applicationScreen, messages.get(DELETE_CHANNEL_SUCCESS), rootLabels.get(RootLabelName.SUCCESS), JOptionPane.INFORMATION_MESSAGE);
                                } else errorReaction(null);
                            } catch (InterruptedException | ExecutionException e) {
                                errorReaction(e);
                            }
                            revaluateChannelTable();
                        }

                        private void errorReaction(Exception e) {
                            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
                            JOptionPane.showMessageDialog(applicationScreen, rootMessages.get(RootMessageName.ERROR_TRY_AGAIN), rootLabels.get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
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
                new CalculationExecutor(applicationScreen, repositoryFactory, channel).execute();
            }
        }
    }

    @Override
    public void calculateChannel(@Nonnull Channel channel) {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        if (channelRepository.isExist(channel.getCode())) {
            new CalculationExecutor(applicationScreen, repositoryFactory, channel).execute();
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
                logger.warn(Messages.Log.EXCEPTION_THROWN, ex);
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
            String message = String.format(messages.get(SEARCH_SUCCESS_QUESTION), channelCode, channel.getName());
            int result = JOptionPane.showConfirmDialog(applicationScreen, message, rootLabels.get(RootLabelName.FOUNDED), JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                new SwingChannelInfoExecutor(applicationScreen, repositoryFactory, this)
                        .registerChannel(channel)
                        .execute();
            }
        } else {
            String message = String.format(messages.get(SEARCH_FAIL), channelCode);
            JOptionPane.showMessageDialog(applicationScreen, message, rootLabels.get(RootLabelName.NOT_FOUNDED), JOptionPane.INFORMATION_MESSAGE);
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
