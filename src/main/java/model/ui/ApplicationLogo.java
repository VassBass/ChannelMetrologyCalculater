package model.ui;

import localization.Labels;
import model.ui.builder.CellBuilder;
import application.ApplicationConfigHolder;
import service.file_extractor.FileExtractor;
import service.file_extractor.ResourceFileExtractor;
import util.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ApplicationLogo extends JWindow {
    private static final String LOGO_FILE_NAME = "logo.png";
    private static final String INIT_TEXT_MESSAGE = Labels.getRootLabels().get("loading") + "...";
    private static final String DEVELOPER = "VassBassApp";

    private final ApplicationConfigHolder configHolder;

    private final JLabel message;

    public ApplicationLogo(ApplicationConfigHolder configHolder){
        super();
        this.configHolder = configHolder;

        String applicationVersion = configHolder.getApplicationVersion();

        JLabel logo = createLogo();

        message = new JLabel(INIT_TEXT_MESSAGE);
        message.setHorizontalAlignment(JLabel.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JLabel developer = new JLabel(DEVELOPER);

        JLabel version = new JLabel(applicationVersion);
        version.setHorizontalAlignment(JLabel.RIGHT);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.add(logo, new CellBuilder().x(0).y(0).width(2).build());
        panel.add(message, new CellBuilder().x(0).y(1).width(2).build());
        panel.add(developer, new CellBuilder().x(0).y(2).width(1).margin(5,5,5,0).build());
        panel.add(version, new CellBuilder().x(1).y(2).width(1).margin(5,5,5,0).build());
        panel.add(progressBar, new CellBuilder().x(0).y(3).width(2).margin(0,5,0,0).build());

        Dimension screenSize = new Dimension(configHolder.getScreenWidth(), configHolder.getScreenHeight());
        this.setSize(340,153);
        this.setLocation(ScreenPoint.center(screenSize, this));
        this.setContentPane(panel);
    }
    
    private JLabel createLogo() {
        FileExtractor fileExtractor = ResourceFileExtractor.getInstance();
        String imagesFolder = configHolder.getImagesFolderPath();
        File logoFile = new File(imagesFolder, LOGO_FILE_NAME);

        if (!logoFile.exists()) {
            String source = "images/logo.png";
            fileExtractor.extract(source, logoFile.getAbsolutePath());
        }

        if (logoFile.exists()) {
            return new JLabel(new ImageIcon(logoFile.getAbsolutePath()));
        } else {
            JLabel result = new JLabel("logo-icon");
            result.setHorizontalAlignment(JLabel.CENTER);
            result.setVerticalAlignment(JLabel.CENTER);
            return result;
        }
    }

    public void setMessage(String message){
        this.message.setText(message);
    }

    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    public void shutdown() {
        this.dispose();
    }
}