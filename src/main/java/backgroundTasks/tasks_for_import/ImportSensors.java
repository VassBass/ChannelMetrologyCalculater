package backgroundTasks.tasks_for_import;

public class ImportSensors /*extends SwingWorker<Integer, Void>*/ {/*
    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    private ArrayList<Sensor>importedSensors, newSensorsList;
    private ArrayList<Integer[]>sensorsIndexes;

    public ImportSensors(MainScreen mainScreen, File exportDataFile){
        super();
        this.mainScreen = mainScreen;
        this.exportDataFile = exportDataFile;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствуют ПИП
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            this.importedSensors = this.sensorsExtraction();
        }catch (Exception e){
            return -1;
        }
        if (this.importedSensors == null){
            return 1;
        }else {
            this.copySensors();
            return 0;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            switch (this.get()) {
                case 1:
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні данні ПВП", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new CompareSensorsDialog(mainScreen, ExportData.SENSORS, newSensorsList, importedSensors, sensorsIndexes);
                        }
                    });
                    break;
                case -1:
                    JOptionPane.showMessageDialog(mainScreen, "Помилка при виконанні імпорту", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Sensor>sensorsExtraction() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<Sensor>sensors = (ArrayList<Sensor>) ois.readObject();
        if (sensors.size() == 0){
            return null;
        }else {
            return sensors;
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
*/}
