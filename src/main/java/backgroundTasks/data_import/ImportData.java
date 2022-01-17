package backgroundTasks.data_import;

public class ImportData /*extends SwingWorker<Integer, Void> */{/*
    private final MainScreen mainScreen;
    private final File importDataFile;
    private final LoadDialog loadDialog;

    private ArrayList<Channel>importedChannels, newChannelsList;
    private ArrayList<Integer[]>channelsIndexes;

    private ArrayList<Sensor>importedSensors, newSensorsList;
    private ArrayList<Integer[]>sensorsIndexes;

    private ArrayList<Calibrator>importedCalibrators, newCalibratorsList;
    private ArrayList<Integer[]>calibratorsIndexes;

    private ArrayList<Worker>importedPersons;
    private ArrayList<String>importedDepartments, importedAreas, importedProcesses, importedInstallations;

    public ImportData(final MainScreen mainScreen, File importDataFile){
        super();
        this.mainScreen = mainScreen;
        this.importDataFile = importDataFile;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствует информация
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        if (this.dataExtraction()){
            if (this.importedSensors.isEmpty()
                    && this.importedChannels.isEmpty()
                    && this.importedCalibrators.isEmpty()
                    && this.importedPersons.isEmpty()
                    && this.importedDepartments.isEmpty()
                    && this.importedAreas.isEmpty()
                    && this.importedProcesses.isEmpty()
                    && this.importedInstallations.isEmpty()){
                return 1;
            }else {
                copySensors();
                copyChannels();
                copyCalibrators();
                return 0;
            }
        }else {
            return -1;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareSensorsDialog(mainScreen, ExportData.ALL_DATA,
                        newSensorsList, importedSensors, sensorsIndexes,
                        newChannelsList, importedChannels, channelsIndexes,
                        newCalibratorsList, importedCalibrators, calibratorsIndexes,
                        importedPersons, importedDepartments, importedAreas, importedProcesses, importedInstallations);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private boolean dataExtraction() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.importDataFile))){
            ArrayList<?>[]data = (ArrayList<?>[]) ois.readObject();

            this.importedSensors = (ArrayList<Sensor>) data[0];
            this.importedChannels = (ArrayList<Channel>) data[1];
            this.importedCalibrators = (ArrayList<Calibrator>) data[2];
            this.importedPersons = (ArrayList<Worker>) data[3];
            this.importedDepartments = (ArrayList<String>) data[4];
            this.importedAreas = (ArrayList<String>) data[5];
            this.importedProcesses = (ArrayList<String>) data[6];
            this.importedInstallations = (ArrayList<String>) data[7];

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void copySensors(){
        ArrayList<Sensor>oldSensorsList = Lists.sensors();
        ArrayList<Integer[]>indexes = new ArrayList<>();
        ArrayList<Sensor>newList = new ArrayList<>();

        for (int o = 0; o< Objects.requireNonNull(oldSensorsList).size(); o++){
            boolean exist = false;
            Sensor old = oldSensorsList.get(o);
            for (int i=0;i<this.importedSensors.size();i++){
                Sensor imp = this.importedSensors.get(i);
                if (old.getName().equals(imp.getName())){
                    exist = true;
                    if (Comparator.sensorsMatch(old, imp)){
                        newList.add(old);
                    }else {
                        indexes.add(new Integer[]{o,i});
                    }
                    break;
                }
            }
            if (!exist){
                newList.add(old);
            }
        }
        for (Sensor imp : this.importedSensors) {
            boolean exist = false;
            for (Sensor old : oldSensorsList) {
                if (imp.getName().equals(old.getName())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                newList.add(imp);
            }
        }

        if (newList.isEmpty()){
            this.newSensorsList = null;
        }else {
            this.newSensorsList = newList;
        }
        if (indexes.isEmpty()){
            this.sensorsIndexes = null;
        }else {
            this.sensorsIndexes = indexes;
        }
    }

    private void copyChannels(){
        ArrayList<Channel>oldChannelsList = Lists.channels();
        ArrayList<Integer[]>indexes = new ArrayList<>();
        ArrayList<Channel>newList = new ArrayList<>();

        for (int o = 0; o< Objects.requireNonNull(oldChannelsList).size(); o++){
            boolean exist = false;
            Channel old = oldChannelsList.get(o);
            for (int i=0;i<this.importedChannels.size();i++){
                Channel imp = this.importedChannels.get(i);
                if (old.getCode().equals(imp.getCode())){
                    exist = true;
                    if (Comparator.channelsMatch(old, imp)){
                        newList.add(old);
                    }else {
                        indexes.add(new Integer[]{o,i});
                    }
                    break;
                }
            }
            if (!exist){
                newList.add(old);
            }
        }
        for (Channel imp : this.importedChannels) {
            boolean exist = false;
            for (Channel old : oldChannelsList) {
                if (imp.getCode().equals(old.getCode())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                newList.add(imp);
            }
        }

        if (newList.isEmpty()){
            this.newChannelsList = null;
        }else {
            this.newChannelsList = newList;
        }
        if (indexes.isEmpty()){
            this.channelsIndexes = null;
        }else {
            this.channelsIndexes = indexes;
        }
    }

    private void copyCalibrators(){
        ArrayList<Calibrator>oldCalibratorsList = Lists.calibrators();
        ArrayList<Integer[]>indexes = new ArrayList<>();
        ArrayList<Calibrator>newList = new ArrayList<>();

        for (int o = 0; o< Objects.requireNonNull(oldCalibratorsList).size(); o++){
            boolean exist = false;
            Calibrator old = oldCalibratorsList.get(o);
            for (int i=0;i<this.importedCalibrators.size();i++){
                Calibrator imp = this.importedCalibrators.get(i);
                if (old.getName().equals(imp.getName())){
                    exist = true;
                    if (Comparator.calibratorsMatch(old, imp)){
                        newList.add(old);
                    }else {
                        indexes.add(new Integer[]{o,i});
                    }
                    break;
                }
            }
            if (!exist){
                newList.add(old);
            }
        }
        for (Calibrator imp : this.importedCalibrators) {
            boolean exist = false;
            for (Calibrator old : oldCalibratorsList) {
                if (imp.getName().equals(old.getName())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                newList.add(imp);
            }
        }

        if (newList.isEmpty()){
            this.newCalibratorsList = null;
        }else {
            this.newCalibratorsList = newList;
        }
        if (indexes.isEmpty()){
            this.calibratorsIndexes = null;
        }else {
            this.calibratorsIndexes = indexes;
        }
    }
*/}
