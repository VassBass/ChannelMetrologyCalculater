package application;

import java.util.Map;
import localization.Labels;
import localization.RootLabelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceInitializer;

import javax.swing.*;
import java.awt.*;

public class ApplicationInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    private void setUkraineLocalization(){
        Map<String, String> labels = Labels.getRootLabels();

        logger.info("Set localization of \"FileChooser\" components");
        //File chooser
        UIManager.put("FileChooser.cancelButtonText", labels.get(RootLabelName.CANCEL));
        UIManager.put("FileChooser.openButtonText", labels.get(RootLabelName.IMPORTING));
        UIManager.put("FileChooser.fileNameLabelText", labels.get(RootLabelName.FILE_NAME));
        UIManager.put("FileChooser.filesOfTypeLabelText", labels.get(RootLabelName.TYPES_OF_FILES));
        UIManager.put("FileChooser.lookInLabelText", labels.get(RootLabelName.DIRECTORY));
        UIManager.put("FileChooser.acceptAllFileFilterText", labels.get(RootLabelName.ALL_FILES));
        UIManager.put("FileChooser.cancelButtonToolTipText", labels.get(RootLabelName.CLOSE_WINDOW));
        UIManager.put("FileChooser.directoryOpenButtonText", labels.get(RootLabelName.OPEN));
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", labels.get(RootLabelName.OPEN_SELECTED_DIRECTORY));
        UIManager.put("FileChooser.openButtonToolTipText", labels.get(RootLabelName.OPEN_SELECTED_FILE));
        UIManager.put("FileChooser.detailsViewActionLabelText", labels.get(RootLabelName.TABLE));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", labels.get(RootLabelName.TABLE));
        UIManager.put("FileChooser.fileDateHeaderText", labels.get(RootLabelName.EDITED));
        UIManager.put("FileChooser.fileNameHeaderText", labels.get(RootLabelName.NAME));
        UIManager.put("FileChooser.fileSizeHeaderText", labels.get(RootLabelName.SIZE));
        UIManager.put("FileChooser.fileTypeHeaderText", labels.get(RootLabelName.TYPE));
        UIManager.put("FileChooser.homeFolderToolTipText", labels.get(RootLabelName.TO_MAIN));
        UIManager.put("FileChooser.listViewActionLabelText", labels.get(RootLabelName.LIST));
        UIManager.put("FileChooser.listViewButtonToolTipText", labels.get(RootLabelName.LIST));
        UIManager.put("FileChooser.newFolderActionLabelText", labels.get(RootLabelName.CREATE_NEW_FOLDER));
        UIManager.put("FileChooser.newFolderToolTipText", labels.get(RootLabelName.CREATE_NEW_FOLDER));
        UIManager.put("FileChooser.refreshActionLabelText", labels.get(RootLabelName.UPDATE));
        UIManager.put("FileChooser.upFolderToolTipText", labels.get(RootLabelName.UP_LEVEL));
        UIManager.put("FileChooser.viewMenuLabelText", labels.get(RootLabelName.VIEW));

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