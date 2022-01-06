package controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileBrowser {

    private static final String DIR_NAME_MAIN = "Support";
    private static final String DIR_NAME_LISTS = "Lists";
    private static final String DIR_NAME_CERTIFICATES = "Certificates";
    private static final String DIR_NAME_EXPORT = "Export";
    private static final String DIR_NAME_FORMS = "forms";
    private static final String DIR_NAME_IMAGES = "images";

    private static final String FILE_NAME_SETTINGS = "settings.dat";
    private static final String FILE_NAME_SENSORS = "Sensors.dat";
    private static final String FILE_NAME_CHANNELS = "Channels.dat";
    private static final String FILE_NAME_DEPARTMENTS = "Departments.dat";
    private static final String FILE_NAME_AREAS = "Areas.dat";
    private static final String FILE_NAME_PROCESSES = "Processes.dat";
    private static final String FILE_NAME_INSTALLATIONS = "Installations.dat";
    private static final String FILE_NAME_CALIBRATORS = "Calibrators.dat";
    private static final String FILE_NAME_MEASUREMENTS = "Measurements.dat";
    private static final String FILE_NAME_PERSONS = "Persons.dat";
    private static final String FILE_NAME_TEMPERATURE_BAD_v3_4 = "form_temperature_bad_v3.4.xls";
    private static final String FILE_NAME_TEMPERATURE_GOOD_v3_4 = "form_temperature_good_v3.4.xls";
    private static final String FILE_NAME_PRESSURE_BAD = "form_pressure_bad.xls";
    private static final String FILE_NAME_PRESSURE_GOOD = "form_pressure_good.xls";
    private static final String FILE_NAME_TEMPERATURE_BAD = "form_temperature_bad.xls";
    private static final String FILE_NAME_TEMPERATURE_GOOD = "form_temperature_good.xls";
    private static final String FILE_NAME_CONSUMPTION_BAD = "form_consumption_bad.xls";
    private static final String FILE_NAME_CONSUMPTION_GOOD = "form_consumption_good.xls";
    private static final String FILE_NAME_CONSUMPTION_ROSEMOUNT_BAD = "form_consumption_rosemount_bad.xls";
    private static final String FILE_NAME_CONSUMPTION_ROSEMOUNT_GOOD = "form_consumption_rosemount_good.xls";
    private static final String FILE_NAME_IMAGE_ANIM_LOAD = "anim_load.gif";
    private static final String FILE_NAME_IMAGE_NAME_LOGO = "name_logo.png";

    private static final File DIR_MAIN = new File(DIR_NAME_MAIN);
    private static final File DIR_LISTS = new File(DIR_MAIN, DIR_NAME_LISTS);
    private static final File DIR_CERTIFICATES = new File(DIR_MAIN, DIR_NAME_CERTIFICATES);
    private static final File DIR_EXPORT = new File(DIR_MAIN, DIR_NAME_EXPORT);
    private static final File DIR_FORMS = new File(DIR_MAIN, DIR_NAME_FORMS);
    private static final File DIR_IMAGES = new File(DIR_MAIN, DIR_NAME_IMAGES);

    private static final File FILE_SETTINGS = new File(DIR_MAIN, FILE_NAME_SETTINGS);
    public static final File FILE_SENSORS = new File(DIR_LISTS, FILE_NAME_SENSORS);
    public static final File FILE_CHANNELS = new File(DIR_LISTS, FILE_NAME_CHANNELS);
    public static final File FILE_DEPARTMENTS = new File(DIR_LISTS, FILE_NAME_DEPARTMENTS);
    public static final File FILE_AREAS = new File(DIR_LISTS, FILE_NAME_AREAS);
    public static final File FILE_PROCESSES = new File(DIR_LISTS, FILE_NAME_PROCESSES);
    public static final File FILE_INSTALLATIONS = new File(DIR_LISTS, FILE_NAME_INSTALLATIONS);
    public static final File FILE_CALIBRATORS = new File(DIR_LISTS, FILE_NAME_CALIBRATORS);
    public static final File FILE_MEASUREMENTS = new File(DIR_LISTS, FILE_NAME_MEASUREMENTS);
    public static final File FILE_PERSONS = new File(DIR_LISTS, FILE_NAME_PERSONS);
    private static final File FILE_TEMPERATURE_BAD_v3_4 = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_BAD_v3_4);
    private static final File FILE_TEMPERATURE_GOOD_v3_4 = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_GOOD_v3_4);
    private static final File FILE_PRESSURE_BAD = new File(DIR_FORMS, FILE_NAME_PRESSURE_BAD);
    private static final File FILE_PRESSURE_GOOD = new File(DIR_FORMS, FILE_NAME_PRESSURE_GOOD);
    private static final File FILE_TEMPERATURE_BAD = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_BAD);
    private static final File FILE_TEMPERATURE_GOOD = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_GOOD);
    private static final File FILE_CONSUMPTION_BAD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_BAD);
    private static final File FILE_CONSUMPTION_GOOD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_GOOD);
    private static final File FILE_CONSUMPTION_ROSEMOUNT_BAD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_ROSEMOUNT_BAD);
    private static final File FILE_CONSUMPTION_ROSEMOUNT_GOOD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_ROSEMOUNT_GOOD);
    public static final File FILE_IMAGE_ANIM_LOAD = new File(DIR_IMAGES, FILE_NAME_IMAGE_ANIM_LOAD);
    public static final File FILE_IMAGE_NAME_LOGO = new File(DIR_IMAGES, FILE_NAME_IMAGE_NAME_LOGO);

    public static void init() throws IOException {
        createDirsIfNotExists();
        createFilesIfNotExists();
        unpackForms();
        unpackImages();
    }

    private static void createFilesIfNotExists() throws IOException {
        if (!FILE_SETTINGS.exists() && !FILE_SETTINGS.createNewFile()) {
            System.out.println("settings file was not created");
        }
        if (!FILE_SENSORS.exists() && !FILE_SENSORS.createNewFile()) {
            System.out.println("sensors list was not created");
        }
        if (!FILE_CHANNELS.exists() && !FILE_CHANNELS.createNewFile()) {
            System.out.println("channels list was not created");
        }
        if (!FILE_DEPARTMENTS.exists() && !FILE_DEPARTMENTS.createNewFile()) {
            System.out.println("departments list was not created");
        }
        if (!FILE_AREAS.exists() && !FILE_AREAS.createNewFile()) {
            System.out.println("areas list was not created");
        }
        if (!FILE_PROCESSES.exists() && !FILE_PROCESSES.createNewFile()) {
            System.out.println("processes list was not created");
        }
        if (!FILE_INSTALLATIONS.exists() && !FILE_INSTALLATIONS.createNewFile()) {
            System.out.println("installations list was not created");
        }
        if (!FILE_CALIBRATORS.exists() && !FILE_CALIBRATORS.createNewFile()) {
            System.out.println("calibrators list was not created");
        }
        if (!FILE_MEASUREMENTS.exists() && !FILE_MEASUREMENTS.createNewFile()) {
            System.out.println("measurements list was not created");
        }
        if (!FILE_PERSONS.exists() && !FILE_PERSONS.createNewFile()){
            System.out.println("persons list was not created");
        }
    }

    private static void createDirsIfNotExists(){
        if (!DIR_MAIN.exists() && !DIR_MAIN.mkdir()) {
            System.out.println("mainDir was not created");
        }

        if (!DIR_LISTS.exists() && !DIR_LISTS.mkdir()) {
            System.out.println("listDir was not created");
        }

        if (!DIR_CERTIFICATES.exists() && !DIR_CERTIFICATES.mkdir()){
            System.out.println("certificatesDir was not created");
        }

        if (!DIR_EXPORT.exists() && !DIR_EXPORT.mkdir()){
            System.out.println("exportDir was not created");
        }

        if (!DIR_FORMS.exists() && !DIR_FORMS.mkdir()){
            System.out.println("formsDir was not created");
        }

        if (!DIR_IMAGES.exists() && !DIR_IMAGES.mkdir()){
            System.out.println("imagesDir was not created");
        }
    }

    private static void unpackForms(){
        try {
            String packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_BAD_v3_4;
            InputStream in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_BAD_v3_4.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_GOOD_v3_4;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_GOOD_v3_4.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_PRESSURE_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_PRESSURE_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_PRESSURE_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_PRESSURE_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_ROSEMOUNT_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_ROSEMOUNT_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_ROSEMOUNT_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_ROSEMOUNT_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void unpackImages(){
        try {
            String packedPath = DIR_NAME_IMAGES + "/" + FILE_NAME_IMAGE_ANIM_LOAD;
            InputStream in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_IMAGE_ANIM_LOAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_IMAGES + "/" + FILE_NAME_IMAGE_NAME_LOGO;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                System.out.println("File no find at: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_IMAGE_NAME_LOGO.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static ObjectInputStream getInputStream(File file) throws IOException {
        return new ObjectInputStream(new FileInputStream(file));
    }

    public static void saveToFile(File file, Object object) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(object);
        out.close();
    }

    public static String getFileExtension(File file){
        if (file.exists()){
            char[]chars = file.getName().toCharArray();
            int index = -1;
            for (int x=0;x<chars.length;x++){
                if (chars[x] == '.'){
                    index = x + 1;
                }
            }
            if (index != -1) {
                return file.getName().substring(index);
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
}
