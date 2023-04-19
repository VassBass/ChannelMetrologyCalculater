package service.channel.list;

import application.ApplicationScreen;
import model.dto.Channel;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.sensor.SensorRepository;
import service.calculation.CalculationExecuter;
import service.channel.info.SwingChannelInfoExecuter;
import service.channel.list.ui.ChannelListInfoTable;
import service.channel.list.ui.ChannelListSearchPanel;
import service.channel.list.ui.ChannelListTable;
import util.DateHelper;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SwingChannelListManager implements ChannelListManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListManager.class);

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
                String message = String.format("Ви впевнені що хочете видалити канал: \"%s\"", channel.getName());
                String title = String.format("Видалити: \"%s\"", channelCode);
                int result = JOptionPane.showConfirmDialog(applicationScreen, message, title, JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    LoadingDialog dialog = LoadingDialog.getInstance();
                    DialogWrapper loadingDialog = new DialogWrapper(applicationScreen, dialog, ScreenPoint.center(applicationScreen, dialog));
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
                                    String message = "Канал був успішно видалений";
                                    JOptionPane.showMessageDialog(applicationScreen, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                                } else errorReaction(null);
                            } catch (InterruptedException | ExecutionException e) {
                                errorReaction(e);
                            }
                            revaluateChannelTable();
                        }

                        private void errorReaction(Exception e) {
                            logger.warn("Exception was thrown", e);
                            String message = "Виникла помилка, будь ласка спробуйте ще раз";
                            JOptionPane.showMessageDialog(applicationScreen, message, "Помилка", JOptionPane.ERROR_MESSAGE);
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
                logger.warn("Exception was thrown!", ex);
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
            String message = String.format("Канал з кодом \"%s\" було знайдено: \"%s\". Відкрити про ньго іформацію?",
                    channelCode, channel.getName());
            int result = JOptionPane.showConfirmDialog(applicationScreen, message, "Знайдено", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                new SwingChannelInfoExecuter(applicationScreen, repositoryFactory, this)
                        .registerChannel(channel)
                        .execute();
            }
        } else {
            String message = String.format("Канал з кодом \"%s\" не знайдено в базі.", channelCode);
            JOptionPane.showMessageDialog(applicationScreen, message, "Не знайдено", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void advancedSearch() {

    }

    @Override
    public void shutdownSearch() {

    }

    @Override
    public void revaluateChannelTable() {
        ChannelListTable channelListTable = context.getElement(ChannelListTable.class);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        List<Channel> channelList = channelRepository.getAll().stream()
                        .sorted(Comparator.comparingLong(c -> {
                            String nextDate = DateHelper.getNextDate(c.getDate(), c.getFrequency());
                            if (nextDate.isEmpty()) return Long.MIN_VALUE;

                            Calendar nextDateCal = DateHelper.stringToDate(nextDate);
                            if (Objects.isNull(nextDateCal)) return Long.MIN_VALUE;

                            long nextDateLong = nextDateCal.getTimeInMillis();
                            long currentDate = Calendar.getInstance().getTimeInMillis();
                            return nextDateLong - currentDate;
                        }))
                .collect(Collectors.toList());
        channelListTable.setChannelList(new ArrayList<>(channelList));
    }
}
