package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileBrowser {
    private static final Logger LOGGER = Logger.getLogger(FileBrowser.class.getName());

    private static final String DIR_NAME_MAIN = "Support";
    private static final String DIR_NAME_CERTIFICATES = "Certificates";
    private static final String DIR_NAME_EXPORT = "Export";
    private static final String DIR_NAME_FORMS = "forms";
    private static final String DIR_NAME_IMAGES = "images";

    private static final String FILE_NAME_SETTINGS = "settings.dat";
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

    public static final File DIR_MAIN = new File(DIR_NAME_MAIN);
    public static final File DIR_CERTIFICATES = new File(DIR_MAIN, DIR_NAME_CERTIFICATES);
    public static final File DIR_EXPORT = new File(DIR_MAIN, DIR_NAME_EXPORT);
    private static final File DIR_FORMS = new File(DIR_MAIN, DIR_NAME_FORMS);
    private static final File DIR_IMAGES = new File(DIR_MAIN, DIR_NAME_IMAGES);

    public static final File FILE_SETTINGS = new File(DIR_MAIN, FILE_NAME_SETTINGS);
    public static final File FILE_PRESSURE_BAD = new File(DIR_FORMS, FILE_NAME_PRESSURE_BAD);
    public static final File FILE_PRESSURE_GOOD = new File(DIR_FORMS, FILE_NAME_PRESSURE_GOOD);
    public static final File FILE_TEMPERATURE_BAD = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_BAD);
    public static final File FILE_TEMPERATURE_GOOD = new File(DIR_FORMS, FILE_NAME_TEMPERATURE_GOOD);
    public static final File FILE_CONSUMPTION_BAD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_BAD);
    public static final File FILE_CONSUMPTION_GOOD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_GOOD);
    public static final File FILE_CONSUMPTION_ROSEMOUNT_BAD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_ROSEMOUNT_BAD);
    public static final File FILE_CONSUMPTION_ROSEMOUNT_GOOD = new File(DIR_FORMS, FILE_NAME_CONSUMPTION_ROSEMOUNT_GOOD);
    public static final File FILE_IMAGE_ANIM_LOAD = new File(DIR_IMAGES, FILE_NAME_IMAGE_ANIM_LOAD);
    public static final File FILE_IMAGE_NAME_LOGO = new File(DIR_IMAGES, FILE_NAME_IMAGE_NAME_LOGO);

    public static File certificateFile(String fileName){
        File file = new File(DIR_CERTIFICATES, fileName);
        if (!file.exists()){
            try {
                if (!file.createNewFile()){
                    System.out.println("certificateFile was not created");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static void init() throws IOException {
        LOGGER.fine("Initialization start...");
        createDirsIfNotExists();
        createFilesIfNotExists();
        unpackForms();
        unpackImages();
        LOGGER.info("Initialization SUCCESS");
    }

    private static void createFilesIfNotExists() throws IOException {
        LOGGER.fine("FileBrowser: create files if not exists");
        if (!FILE_SETTINGS.exists() && !FILE_SETTINGS.createNewFile()) {
            LOGGER.log(Level.WARNING, "FileBrowser: file \"" + FILE_NAME_SETTINGS + "\" was not created!");
        }
    }

    private static void createDirsIfNotExists(){
        LOGGER.fine("FileBrowser: create dirs if not exists");
        if (!DIR_MAIN.exists() && !DIR_MAIN.mkdir()) {
            LOGGER.log(Level.WARNING, "FileBrowser: dir \"" + DIR_NAME_MAIN + "\" was not created!");
        }

        if (!DIR_CERTIFICATES.exists() && !DIR_CERTIFICATES.mkdir()){
            LOGGER.log(Level.WARNING, "FileBrowser: dir \"" + DIR_NAME_CERTIFICATES + "\" was not created!");
        }

        if (!DIR_EXPORT.exists() && !DIR_EXPORT.mkdir()){
            LOGGER.log(Level.WARNING, "FileBrowser: dir \"" + DIR_NAME_EXPORT + "\" was not created!");
        }

        if (!DIR_FORMS.exists() && !DIR_FORMS.mkdir()){
            LOGGER.log(Level.WARNING, "FileBrowser: dir \"" + DIR_NAME_FORMS + "\" was not created!");
        }

        if (!DIR_IMAGES.exists() && !DIR_IMAGES.mkdir()){
            LOGGER.log(Level.WARNING, "FileBrowser: dir \"" + DIR_NAME_IMAGES + "\" was not created!");
        }
    }

    private static void unpackForms(){
        LOGGER.fine("FileBrowser: unpack forms from resources");
        String packedPath;
        InputStream in;
        try {
            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_TEMPERATURE_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_TEMPERATURE_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_PRESSURE_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_PRESSURE_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_PRESSURE_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_PRESSURE_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_ROSEMOUNT_BAD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_ROSEMOUNT_BAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_FORMS + "/" + FILE_NAME_CONSUMPTION_ROSEMOUNT_GOOD;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_CONSUMPTION_ROSEMOUNT_GOOD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }
        }catch (Exception ex){
            LOGGER.log(Level.WARNING, "FileBrowser: Exception while unpack forms!", ex);
        }
    }

    private static void unpackImages(){
        LOGGER.fine("FileBrowser: Uunpack images from resources");
        try {
            String packedPath = DIR_NAME_IMAGES + "/" + FILE_NAME_IMAGE_ANIM_LOAD;
            InputStream in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_IMAGE_ANIM_LOAD.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }

            packedPath = DIR_NAME_IMAGES + "/" + FILE_NAME_IMAGE_NAME_LOGO;
            in = FileBrowser.class.getClassLoader().getResourceAsStream(packedPath);
            if (in == null){
                LOGGER.log(Level.WARNING, "FileBrowser: file not found: " + packedPath);
            }else {
                Path unpackingPath = Paths.get(FILE_IMAGE_NAME_LOGO.getAbsolutePath());
                Files.copy(in, unpackingPath, REPLACE_EXISTING);
                in.close();
            }
        }catch (Exception ex){
            LOGGER.log(Level.WARNING, "FileBrowser: Exception while unpack images!", ex);
        }
    }
}