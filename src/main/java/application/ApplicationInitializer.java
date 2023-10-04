package application;

import localization.label.Labels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceInitializer;

import javax.swing.*;
import java.awt.*;

public class ApplicationInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    private void setUkraineLocalization(){
        Labels labels = Labels.getInstance();

        logger.info("Set Ukrainian localization of \"FileChooser\" components");
        //File chooser
        UIManager.put("FileChooser.cancelButtonText", labels.cancel);
        UIManager.put("FileChooser.openButtonText", labels.importing);
        UIManager.put("FileChooser.fileNameLabelText", labels.fileName);
        UIManager.put("FileChooser.filesOfTypeLabelText", labels.typesOfFiles);
        UIManager.put("FileChooser.lookInLabelText", labels.directory);
        UIManager.put("FileChooser.acceptAllFileFilterText", labels.allFiles);
        UIManager.put("FileChooser.cancelButtonToolTipText", labels.closeWindow);
        UIManager.put("FileChooser.directoryOpenButtonText", labels.open);
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", labels.openSelectedDirectory);
        UIManager.put("FileChooser.openButtonToolTipText", labels.openSelectedFile);
        UIManager.put("FileChooser.detailsViewActionLabelText", labels.table);
        UIManager.put("FileChooser.detailsViewButtonToolTipText", labels.table);
        UIManager.put("FileChooser.fileDateHeaderText", labels.edited);
        UIManager.put("FileChooser.fileNameHeaderText", labels.name);
        UIManager.put("FileChooser.fileSizeHeaderText", labels.size);
        UIManager.put("FileChooser.fileTypeHeaderText", labels.type);
        UIManager.put("FileChooser.homeFolderToolTipText", labels.toMain);
        UIManager.put("FileChooser.listViewActionLabelText", labels.list);
        UIManager.put("FileChooser.listViewButtonToolTipText", labels.list);
        UIManager.put("FileChooser.newFolderActionLabelText", labels.createNewFolder);
        UIManager.put("FileChooser.newFolderToolTipText", labels.createNewFolder);
        UIManager.put("FileChooser.refreshActionLabelText", labels.update);
        UIManager.put("FileChooser.upFolderToolTipText", labels.upLevel);
        UIManager.put("FileChooser.viewMenuLabelText", labels.view);

        //ToolTip
        UIManager.put("ToolTip.background", Color.BLACK);
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

    @Override
    public void init() {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        setUkraineLocalization();
    }
}